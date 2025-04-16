package com.example.todolist.presentation.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.snapfood.R
import com.example.snapfood.domain.model.Priority
import com.example.snapfood.domain.model.TaskStatus
import com.example.snapfood.domain.model.ToDoTask
import com.example.snapfood.presentation.theme.TodoListTheme
import com.example.snapfood.presentation.ui.common.CommonCard
import com.example.snapfood.presentation.ui.home.TaskScreenEvent
import com.example.snapfood.presentation.ui.home.TaskScreenState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: TaskScreenState,
    modifier: Modifier = Modifier,
    onEvent: (TaskScreenEvent) -> Unit
) {
    // State for controlling bottom sheet visibility
    var showAddTaskSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskSheet = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_task)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            HomeHeader()
            SearchBox(
                query = state.searchQuery,
                onQueryChange = { onEvent(TaskScreenEvent.OnSearchQueryChange(it)) }
            )

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                if (state.tasks.isEmpty()) {
                    EmptyTasksMessage()
                } else {
                    TasksList(
                        tasks = state.tasks,
                        onTaskClick = { onEvent(TaskScreenEvent.OnTaskClick(it)) },
                        onEditTask = { onEvent(TaskScreenEvent.OnEditTaskClick(it)) },
                        onDeleteTask = { onEvent(TaskScreenEvent.OnDeleteTaskClick(it)) },
                        onTaskStatusChange = { id, status ->
                            onEvent(TaskScreenEvent.OnTaskStatusChange(id, status))
                        }
                    )
                }
            }
        }

        // Add Task Bottom Sheet
        AddTaskBottomSheet(
            isVisible = showAddTaskSheet,
            onDismiss = { showAddTaskSheet = false },
            onSave = { task -> onEvent(TaskScreenEvent.OnAddTaskClick(task)) }
        )
    }
}

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun SearchBox(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(stringResource(R.string.search_tasks))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

@Composable
fun EmptyTasksMessage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_tasks_found),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun TasksList(
    tasks: List<ToDoTask>,
    onTaskClick: (Int) -> Unit,
    onEditTask: (Int) -> Unit,
    onDeleteTask: (Int) -> Unit,
    onTaskStatusChange: (Int, TaskStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 88.dp) // Give space for FAB
    ) {
        items(
            items = tasks,
            key = { it.id }
        ) { task ->
            TaskCard(
                task = task,
                onClick = { onTaskClick(task.id) },
                onEditClick = { onEditTask(task.id) },
                onDeleteClick = { onDeleteTask(task.id) },
                onStatusChange = { status -> onTaskStatusChange(task.id, status) }
            )
        }
    }
}

object TaskCardDefaults {
    val CardCornerRadius = 12.dp
    val ContentPadding = 16.dp
    val SpaceBetweenElements = 8.dp
    val PriorityIndicatorSize = 16.dp

    fun getPriorityColor(priority: Priority): Color {
        return when (priority) {
            Priority.HIGH -> Color(0xFFE57373) // Red
            Priority.MEDIUM -> Color(0xFFFFB74D) // Orange
            Priority.LOW -> Color(0xFF81C784) // Green
            Priority.NONE -> Color(0xFF90A4AE) // Blue-grey
        }
    }

    fun getStatusColor(status: TaskStatus): Color {
        return when (status) {
            TaskStatus.DONE -> Color(0xFF66BB6A) // Green
            TaskStatus.DOING -> Color(0xFFFFB74D) // Orange
            TaskStatus.UNDONE -> Color(0xFFE57373) // Red
            TaskStatus.NONE -> Color(0xFFE23334)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: ToDoTask,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onStatusChange: (TaskStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    CommonCard(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(TaskCardDefaults.CardCornerRadius))
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(TaskCardDefaults.ContentPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Priority indicator
                    Box(
                        modifier = Modifier
                            .size(TaskCardDefaults.PriorityIndicatorSize)
                            .clip(CircleShape)
                            .background(TaskCardDefaults.getPriorityColor(task.priority))
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Status chip
                FilterChip(
                    selected = false,
                    onClick = {
                        // Cycle through statuses
                        val newStatus = when(task.status) {
                            TaskStatus.UNDONE -> TaskStatus.DOING
                            TaskStatus.DOING -> TaskStatus.DONE
                            TaskStatus.DONE -> TaskStatus.UNDONE
                            TaskStatus.NONE -> TaskStatus.UNDONE
                        }
                        onStatusChange(newStatus)
                    },
                    label = {
                        Text(
                            text = task.status.name,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = TaskCardDefaults.getStatusColor(task.status).copy(alpha = 0.2f),
                        labelColor = TaskCardDefaults.getStatusColor(task.status)
                    )
                )
            }

            Spacer(modifier = Modifier.height(TaskCardDefaults.SpaceBetweenElements))

            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(TaskCardDefaults.SpaceBetweenElements))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.addedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row {
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit_task),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete_task),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSave: (ToDoTask) -> Unit,
    modifier: Modifier = Modifier
) {
    val taskTitle = remember { mutableStateOf("") }
    val taskDescription = remember { mutableStateOf("") }
    val selectedPriority = remember { mutableStateOf(Priority.NONE) }

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_new_task),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Task Title
                OutlinedTextField(
                    value = taskTitle.value,
                    onValueChange = { taskTitle.value = it },
                    label = { Text(stringResource(R.string.task_title)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Task Description
                OutlinedTextField(
                    value = taskDescription.value,
                    onValueChange = { taskDescription.value = it },
                    label = { Text(stringResource(R.string.task_description)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Task Priority
                Text(
                    text = stringResource(R.string.priority),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Priority.entries.forEach { priority ->
                        val isSelected = priority == selectedPriority.value
                        val color = TaskCardDefaults.getPriorityColor(priority)

                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedPriority.value = priority },
                            label = { Text(priority.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = color.copy(alpha = 0.2f),
                                selectedLabelColor = color
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            taskTitle.value = ""
                            taskDescription.value = ""
                            selectedPriority.value = Priority.NONE
                            onDismiss()
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (taskTitle.value.isNotBlank()) {
                                // Create new task and save
                                val newTask = ToDoTask(
                                    id = 0,
                                    title = taskTitle.value,
                                    description = taskDescription.value,
                                    addedDate = LocalDate.now(),
                                    status = TaskStatus.UNDONE,
                                    priority = selectedPriority.value
                                )
                                onSave(newTask)

                                // Reset form
                                taskTitle.value = ""
                                taskDescription.value = ""
                                selectedPriority.value = Priority.NONE
                                onDismiss()
                            }
                        },
                        enabled = taskTitle.value.isNotBlank()
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeHeaderPreview() {
    TodoListTheme {
        HomeHeader()
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBoxPreview() {
    TodoListTheme {
        SearchBox(
            query = "Meeting",
            onQueryChange = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun TaskCardPreview() {
    TodoListTheme {
        TaskCard(
            task = ToDoTask(
                id = 1,
                title = "Finish project presentation",
                description = "Complete slides and prepare talking points for the team meeting on Friday",
                addedDate = LocalDate.now(),
                status = TaskStatus.DOING,
                priority = Priority.HIGH
            ),
            onClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onStatusChange = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun HomeScreenPreview() {
    TodoListTheme {
        HomeScreen(
            state = TaskScreenState(
                tasks = listOf(
                    ToDoTask(
                        id = 1,
                        title = "Finish project presentation",
                        description = "Complete slides and prepare talking points for the team meeting on Friday",
                        addedDate = LocalDate.now(),
                        status = TaskStatus.DOING,
                        priority = Priority.HIGH
                    ),
                    ToDoTask(
                        id = 2,
                        title = "Buy groceries",
                        description = "Milk, eggs, bread, and vegetables",
                        addedDate = LocalDate.now().minusDays(1),
                        status = TaskStatus.UNDONE,
                        priority = Priority.MEDIUM
                    ),
                    ToDoTask(
                        id = 3,
                        title = "Call mom",
                        description = "Remember to wish her happy birthday",
                        addedDate = LocalDate.now().minusDays(2),
                        status = TaskStatus.DONE,
                        priority = Priority.LOW
                    )
                )
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewAddTaskBottomSheet(
    isVisible: Boolean = true,
    onDismiss: () -> Unit = {},
    onSave: (ToDoTask) -> Unit = {}
) {
    TodoListTheme {
        AddTaskBottomSheet(
            isVisible = true,
            onDismiss = onDismiss,
            onSave = onSave
        )
    }
}