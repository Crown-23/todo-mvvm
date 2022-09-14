package com.crown.todo.data.source

import androidx.lifecycle.LiveData
import com.crown.todo.data.Result
import com.crown.todo.data.Task

/**
 * Description：Main entry point for accessing tasks data.
 */
interface TasksDataSource {
    fun observeTasks(): LiveData<Result<List<Task>>>

    fun getTasks(): Result<List<Task>>

    fun refreshTasks()

    fun observeTask(taskId: String): LiveData<Result<Task>>

    suspend fun getTask(taskId: String): Result<Task>

    fun refreshTask(taskId: String)

    suspend fun saveTask(task: Task)

    suspend fun completeTask(task: Task)

    suspend fun completeTask(taskId: String)

    suspend fun activateTask(task: Task)

    suspend fun activateTask(taskId: String)

    suspend fun clearCompletedTasks()

    suspend fun deleteAllTasks()

    suspend fun deleteTask(taskId: String)
}