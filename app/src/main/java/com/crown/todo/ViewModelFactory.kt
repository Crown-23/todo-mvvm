package com.crown.todo

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.crown.todo.addedittask.AddEditTaskViewModel
import com.crown.todo.data.source.TasksRepository
import com.crown.todo.statistics.StatisticsViewModel
import com.crown.todo.taskdetail.TaskDetailViewModel
import com.crown.todo.tasks.TasksViewModel

/**
 * Description：Factory for all ViewModels.
 */
class ViewModelFactory(
    private val tasksRepository: TasksRepository,
    owner: SavedStateRegistryOwner, defaultArgs: Bundle? = null
) :
    AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return with(modelClass) {
            when {
                isAssignableFrom(TasksViewModel::class.java) ->
                    TasksViewModel(tasksRepository, handle)
                isAssignableFrom(AddEditTaskViewModel::class.java) ->
                    AddEditTaskViewModel(tasksRepository)
                isAssignableFrom(TaskDetailViewModel::class.java) ->
                    TaskDetailViewModel(tasksRepository)
                isAssignableFrom(StatisticsViewModel::class.java) ->
                    StatisticsViewModel(tasksRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
    }
}