package com.crown.todo.addedittask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crown.todo.Event
import com.crown.todo.R
import com.crown.todo.data.Result
import com.crown.todo.data.Task
import com.crown.todo.data.source.TasksRepository
import kotlinx.coroutines.launch

/**
 * Description：ViewModel for the Add/Edit screen.
 */
class AddEditTaskViewModel(private val tasksRepository: TasksRepository) : ViewModel() {
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _taskUpdatedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent: MutableLiveData<Event<Unit>> = _taskUpdatedEvent

    private var taskId: String? = null

    private var isNewTask: Boolean = false

    private var taskCompleted = false

    fun start(taskId: String?) {
        this.taskId = taskId
        if (taskId == null) {
            // No need to populate, it's a new task
            isNewTask = true
            return
        }

        isNewTask = false

        viewModelScope.launch {
            tasksRepository.getTask(taskId).let {
                if (it is Result.Success) {
                    onTaskLoaded(it.data)
                }
            }
        }
    }

    private fun onTaskLoaded(task: Task) {
        title.value = task.title
        description.value = task.description
        taskCompleted = task.isCompleted
    }

    // Called when clicking on fab.
    fun saveTask() {
        val currentTitle = title.value
        val currentDescription = description.value

        if (currentTitle == null || currentDescription == null) {
            _snackbarText.value = Event(R.string.empty_task_message)
            return
        }
        if (Task(currentTitle, currentDescription).isEmpty) {
            _snackbarText.value = Event(R.string.empty_task_message)
            return
        }

        val currentTaskId = taskId
        val task = if (isNewTask || currentTaskId == null) {
            Task(currentTitle, currentDescription)
        } else {
            Task(currentTitle, currentDescription, taskCompleted, currentTaskId)
        }
        addEditTask(task)
    }

    private fun addEditTask(newTask: Task) = viewModelScope.launch {
        tasksRepository.saveTask(newTask)
        _taskUpdatedEvent.value = Event(Unit)
    }


}

