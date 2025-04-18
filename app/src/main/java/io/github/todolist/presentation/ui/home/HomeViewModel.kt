package io.github.todolist.presentation.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.todolist.domain.model.Resources
import io.github.todolist.domain.model.TaskStatus
import io.github.todolist.domain.model.ToDoTask
import io.github.todolist.domain.usecase.AddTaskUseCase
import io.github.todolist.domain.usecase.EditTaskUseCase
import io.github.todolist.domain.usecase.GetAllTasksUseCase
import io.github.todolist.domain.usecase.RemoveTaskUseCase
import io.github.todolist.domain.usecase.SyncTasksUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val editTaskUseCase: EditTaskUseCase,
    private val removeTaskUseCase: RemoveTaskUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val syncTasksUseCase: SyncTasksUseCase,
    private val searchDebouncer: SearchDebouncer
) : ViewModel() {

    private val _state = MutableStateFlow(TaskScreenState())
    val state = _state.asStateFlow()

    init {
        setupSearchDebouncing()
        syncTasks()
    }

    fun syncTasks() {
        viewModelScope.launch {
            try {
                // Clear previous errors and set status to SYNCING
                _state.update {
                    it.copy(
                        syncStatus = SyncStatus.SYNCING,
                        errorMessage = ""
                    )
                }

                syncTasksUseCase().collect { result ->
                    when (result) {
                        is Resources.Success -> {
                            _state.update {
                                it.copy(syncStatus = SyncStatus.SUCCESS)
                            }

                            // Auto-reset success status after delay
                            delay(2000)
                            _state.update {
                                it.copy(syncStatus = SyncStatus.NONE)
                            }

                            // Load fresh data
                            loadInitialTasks()
                        }

                        is Resources.Error -> {
                            _state.update {
                                it.copy(
                                    syncStatus = SyncStatus.ERROR,
                                    errorMessage = result.message ?: "Sync failed"
                                )
                            }

                            // Still load local data on error
                            loadInitialTasks()
                        }

                        is Resources.Loading -> {
                            // Status is already SYNCING, nothing to do here
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        syncStatus = SyncStatus.ERROR,
                        errorMessage = "Unexpected error: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    private fun setupSearchDebouncing() {
        viewModelScope.launch {
            searchDebouncer.getQueryFlow()
                .collect { query ->
                    if (query.isNotBlank()) {
                        performSearch(query)
                    } else {
                        clearSearchResults()
                        loadInitialTasks()
                    }
                }
        }
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnSearchQueryChange -> updateSearchQuery(event.query)
            HomeScreenEvent.LoadTasks -> loadInitialTasks()
            is HomeScreenEvent.OnAddTaskClick -> saveTask(event.toDoTask)
            is HomeScreenEvent.OnDeleteTaskClick -> deleteTask(event.task)
            is HomeScreenEvent.OnEditTaskClick -> editTask(event.editedTask)
            is HomeScreenEvent.OnTaskClick -> onTaskClick(event.task)
            is HomeScreenEvent.OnTaskStatusChange -> changeTaskStatus(event.status)
            is HomeScreenEvent.OnStatusFilterChange -> updateStatusFilter(event.status)
        }
    }

    private fun updateStatusFilter(status: TaskStatus?) {
        _state.update { it.copy(statusFilter = status) }

        // Reload tasks with the new filter
        if (_state.value.searchQuery.isBlank()) {
            loadInitialTasks()
        } else {
            searchDebouncer.setQuery(_state.value.searchQuery) // This will trigger search with filter
        }
    }

    private fun changeTaskStatus(status: TaskStatus) {
        _state.update {
            it.copy(
                currentTask = ToDoTask(
                    status = status
                )
            )
        }
    }

    private fun onTaskClick(task: ToDoTask){
        Log.d(TAG, "onTaskClick: clicked on task with id = $task")
    }

    private fun editTask(editedTask: ToDoTask){
        viewModelScope.launch {
            editTaskUseCase(editedTask)
                .onStart { setLoading(true) }
                .catch { handleError(it) }
                .collect {
                    setLoading(false)
                    loadInitialTasks() // Reload tasks after editing
                }
        }
    }

    private fun deleteTask(task: ToDoTask) {
        viewModelScope.launch {
            removeTaskUseCase(task).
                    onStart { setLoading(true) }.
                    catch {handleError(it)}.
                    collect {
                        loadInitialTasks()
                        setLoading(false)
                    }
        }
    }

    private fun saveTask(toDoTask: ToDoTask){
        viewModelScope.launch {
            addTaskUseCase(task = toDoTask).
                    onStart { setLoading(true) }.
                    catch { handleError(it) }.
                    collect{
                        setLoading(false)
                        loadInitialTasks()
                    }
        }
    }

    private fun loadInitialTasks() {
        viewModelScope.launch {
            getAllTasksUseCase().
                onStart { setLoading(true) }.
                catch { error ->
                    handleError(error)
                }.
                collect { result ->
                    handleSearchResult(result)
                }
        }
    }

    private fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
        searchDebouncer.setQuery(query)
    }

    private suspend fun performSearch(query: String) {
        getAllTasksUseCase()
            .onStart { setLoading(true) }
            .catch { error -> handleError(error) }
            .collect { result ->
                val filteredBySearch =
                    result.data?.filter { it.title.contains(query, ignoreCase = true) }

                // Apply status filter on top of search results if needed
                val filteredList = if (_state.value.statusFilter != null) {
                    filteredBySearch?.filter { it.status == _state.value.statusFilter }
                } else {
                    filteredBySearch
                }

                handleSearchResult(Resources.Success(filteredList))
            }
    }

    private fun clearSearchResults() {
        _state.update {
            it.copy(
                tasks = emptyList(),
                isLoading = false,
            )
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

    private fun handleError(error: Throwable) {
        _state.update {
            it.copy(
                isLoading = false,
                tasks = emptyList(),
            )
        }
    }

    private fun handleSearchResult(result: Resources<List<ToDoTask>>) {
        when (result) {
            is Resources.Success -> {
                // Apply status filter if one is set
                val filteredTasks = if (_state.value.statusFilter != null) {
                    result.data?.filter { it.status == _state.value.statusFilter } ?: emptyList()
                } else {
                    result.data?.map { it } ?: emptyList()
                }

                _state.update { currentState ->
                    currentState.copy(
                        tasks = filteredTasks,
                        isLoading = false
                    )
                }
            }
            is Resources.Error -> handleSearchError()
            is Resources.Loading -> setLoading(result.isLoading)
        }
    }

    private fun updateCharactersList(tasks: List<ToDoTask>?) {
        _state.update { currentState ->
            currentState.copy(
                tasks = tasks?.map { it } ?: emptyList(),
                isLoading = false,
            )
        }
    }

    private fun handleSearchError() {
        _state.update {
            it.copy(
                isLoading = false,
                tasks = emptyList(),
            )
        }
    }

    companion object {
        val TAG = Companion::class.java.simpleName
    }
}

