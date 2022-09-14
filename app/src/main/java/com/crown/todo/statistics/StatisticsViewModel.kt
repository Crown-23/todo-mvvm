package com.crown.todo.statistics

import androidx.lifecycle.*
import com.crown.todo.data.Result
import com.crown.todo.data.Task
import com.crown.todo.data.source.TasksRepository
import kotlinx.coroutines.launch

/**
 * Description：ViewModel for the statistics screen.
 */
class StatisticsViewModel(private val tasksRepository: TasksRepository) : ViewModel() {
    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val tasks: LiveData<Result<List<Task>>> = tasksRepository.observeTasks()

    val empty: LiveData<Boolean> = tasks.map { (it as? Result.Success)?.data.isNullOrEmpty() }

    private val stats: LiveData<StatsResult?> = tasks.map {
        if (it is Result.Success) {
            getActiveAndCompletedStats(it.data)
        } else {
            null
        }
    }

    val activeTasksPercent = stats.map {
        it?.activeTasksPercent ?: 0f
    }

    val completedTasksPercent = stats.map {
        it?.completedTasksPercent ?: 0f
    }

    fun refresh() {
        _dataLoading.value = true
        viewModelScope.launch {
            tasksRepository.refreshTasks()
            _dataLoading.value = false
        }
    }

}

