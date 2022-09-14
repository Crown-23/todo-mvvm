package com.crown.todo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crown.todo.data.source.TasksDataSource

/**
 * Description：Implementation of a remote data source with static access to the data for easy testing.
 */
object FakeTasksRemoteDataSource : TasksDataSource {
    private var TASKS_SERVICE_DATA: LinkedHashMap<String, Task> = LinkedHashMap()

    private val observableTasks = MutableLiveData<Result<List<Task>>>()

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        TODO("Not yet implemented")
    }

    override fun getTasks(): Result<List<Task>> {
        return Result.Success(TASKS_SERVICE_DATA.values.toList())
    }

    override fun refreshTasks() {
        observableTasks.postValue(getTasks())
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        TASKS_SERVICE_DATA[taskId]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Could not find task"))
    }

    override fun refreshTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(task: Task) {
        TASKS_SERVICE_DATA[task.id] = task
    }

    override suspend fun completeTask(task: Task) {
        // TODO: optimized
        TASKS_SERVICE_DATA[task.id]?.isCompleted = true
    }

    override suspend fun completeTask(taskId: String) {
        // TODO: optimized
        TASKS_SERVICE_DATA[taskId]?.isCompleted = true
    }

    override suspend fun activateTask(task: Task) {
        // TODO: optimized
        TASKS_SERVICE_DATA[task.id]?.isCompleted = false
    }

    override suspend fun activateTask(taskId: String) {
        // TODO: optimized
        TASKS_SERVICE_DATA[taskId]?.isCompleted = false
    }

    override suspend fun clearCompletedTasks() {
        TASKS_SERVICE_DATA.filterValues {
            !it.isCompleted
        }
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TASKS_SERVICE_DATA.remove(taskId)
        refreshTasks()
    }
}