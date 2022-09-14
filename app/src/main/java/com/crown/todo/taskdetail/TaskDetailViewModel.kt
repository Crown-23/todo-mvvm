package com.crown.todo.taskdetail

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.crown.todo.Event
import com.crown.todo.R
import com.crown.todo.data.Result
import com.crown.todo.data.Task
import com.crown.todo.data.source.TasksRepository
import kotlinx.coroutines.launch

/**
 * Description：ViewModel for the Details screen.
 */
class TaskDetailViewModel(private val tasksRepository: TasksRepository) : ViewModel() {
    private val _taskId = MutableLiveData<String>()

    private val _task = _taskId.switchMap { taskId ->
        tasksRepository.observeTask(taskId).map { computeResult(it) }
    }
    val task: LiveData<Task?> = _task

    val isDataAvailable: LiveData<Boolean> = _task.map { it != null }

    val completed: LiveData<Boolean> = _task.map { it?.isCompleted ?: false }

    private val _editTaskEvent = MutableLiveData<Event<Unit>>()
    val editTaskEvent: LiveData<Event<Unit>> = _editTaskEvent

    private val _deleteTaskEvent = MutableLiveData<Event<Unit>>()
    val deleteTaskEvent: LiveData<Event<Unit>> = _deleteTaskEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    fun start(taskId: String) {
        // If we already loaded, return (might be a config change)
        if (taskId == _taskId.value) return
        // Trigger the load
        _taskId.value = taskId
    }

    private fun computeResult(taskResult: Result<Task>): Task? {
        return if (taskResult is Result.Success) {
            taskResult.data
        } else {
            showSnackbarMessage(R.string.loading_tasks_error)
            null
        }
    }

    fun editTask() {
        _editTaskEvent.value = Event(Unit)
    }

    fun deleteTask() = viewModelScope.launch {
        _taskId.value?.let {
            tasksRepository.deleteTask(it)
            _deleteTaskEvent.value = Event(Unit)
        }
    }

    fun setCompleted(completed: Boolean) = viewModelScope.launch {
        _taskId.value?.let {
            if (completed) {
                tasksRepository.completeTask(it)
                showSnackbarMessage(R.string.task_marked_complete)
            } else {
                tasksRepository.activateTask(it)
                showSnackbarMessage(R.string.task_marked_active)
            }
        }
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = Event(message)
    }

}

