package com.example.snapfood

import com.example.snapfood.domain.model.*
import com.example.snapfood.domain.usecase.*
import com.example.snapfood.presentation.ui.home.HomeScreenEvent
import com.example.snapfood.presentation.ui.home.HomeViewModel
import com.example.snapfood.presentation.ui.home.SearchDebouncer
import com.example.snapfood.presentation.ui.home.SyncStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import org.mockito.Mockito.*
import org.mockito.kotlin.*
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: HomeViewModel

    private val addTaskUseCase: com.github.todolist.domain.usecase.AddTaskUseCase = mock()
    private val editTaskUseCase: EditTaskUseCase = mock()
    private val removeTaskUseCase: RemoveTaskUseCase = mock()
    private val getAllTasksUseCase: GetAllTasksUseCase = mock()
    private val syncTasksUseCase: com.github.todolist.domain.usecase.SyncTasksUseCase = mock()
    private val searchDebouncer: SearchDebouncer = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Default query flow to empty (so it doesn't block)
        whenever(searchDebouncer.getQueryFlow()).thenReturn(MutableSharedFlow())

        viewModel = HomeViewModel(
            addTaskUseCase,
            editTaskUseCase,
            removeTaskUseCase,
            getAllTasksUseCase,
            syncTasksUseCase,
            searchDebouncer
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadInitialTasks emits success and updates state`() = runTest {
        val tasks = listOf(ToDoTask(id = 1, title = "Test Task"))
        whenever(getAllTasksUseCase()).thenReturn(flowOf(Resources.Success(tasks)))

        viewModel.onEvent(HomeScreenEvent.LoadTasks)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(tasks, state.tasks)
    }

    @Test
    fun `loadInitialTasks emits error and clears task list`() = runTest {
        whenever(getAllTasksUseCase()).thenReturn(flow { throw RuntimeException("Something went wrong") })

        viewModel.onEvent(HomeScreenEvent.LoadTasks)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.tasks.isEmpty())
    }

    @Test
    fun `saveTask calls use case and reloads tasks`() = runTest {
        val task = ToDoTask(id = 99, title = "New Task")

        whenever(addTaskUseCase(task)).thenReturn(flowOf(Resources.Success(1)))
        whenever(getAllTasksUseCase()).thenReturn(flowOf(Resources.Success(listOf(task))))

        viewModel.onEvent(HomeScreenEvent.OnAddTaskClick(task))

        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(task.title, state.tasks.first().title)
    }

    @Test
    fun `deleteTask calls use case and reloads tasks`() = runTest {
        val task = ToDoTask(id = 55, title = "Delete Me")

        whenever(removeTaskUseCase(task)).thenReturn(flowOf(Resources.Success(1)))
        whenever(getAllTasksUseCase()).thenReturn(flowOf(Resources.Success(emptyList())))

        viewModel.onEvent(HomeScreenEvent.OnDeleteTaskClick(task))

        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.tasks.isEmpty())
    }

    @Test
    fun `editTask updates task and reloads`() = runTest {
        val task = ToDoTask(id = 1, title = "Old Title")
        val editedTask = task.copy(title = "New Title")

        whenever(editTaskUseCase(editedTask)).thenReturn(flowOf(Resources.Success(1)))
        whenever(getAllTasksUseCase()).thenReturn(flowOf(Resources.Success(listOf(editedTask))))

        viewModel.onEvent(HomeScreenEvent.OnEditTaskClick(editedTask))

        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("New Title", state.tasks.first().title)
    }

    @Test
    fun `syncTasks emits success then resets syncStatus`() = runTest {
        whenever(syncTasksUseCase()).thenReturn(flowOf(Resources.Success(Unit)))
        whenever(getAllTasksUseCase()).thenReturn(flowOf(Resources.Success(emptyList())))

        viewModel.onEvent(HomeScreenEvent.LoadTasks)

        viewModel.syncTasks() // call manually

        advanceTimeBy(2000) // for the delay

        val state = viewModel.state.value
        assertEquals(SyncStatus.NONE, state.syncStatus)
    }
}