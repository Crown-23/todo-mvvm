package com.crown.todo.data.source

import androidx.lifecycle.LiveData
import com.crown.todo.data.Result
import com.crown.todo.data.Task

/**
 * Description：Interface to the data layer.
 */
interface TasksRepository {

    fun observeTasks(): LiveData<Result<List<Task>>>

    fun getTasks(forceUpdate: Boolean = false): Result<List<Task>>

    suspend fun refreshTasks()

    fun observeTask(taskId: String): LiveData<Result<Task>>

    suspend fun getTask(taskId: String, forceUpdate: Boolean = false): Result<Task>

    fun refreshTask(taskId: String)

    suspend fun saveTask(task: Task)

    suspend fun completeTask(task: Task)

    suspend fun completeTask(taskId: String)

    suspend fun activateTask(task: Task)

    suspend fun activateTask(taskId: String)

    suspend fun clearCompletedTasks()

    fun deleteAllTasks()

    suspend fun deleteTask(taskId: String)
}