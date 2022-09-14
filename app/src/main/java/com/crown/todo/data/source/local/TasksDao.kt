package com.crown.todo.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.crown.todo.data.Task

/**
 * Description：Data Access Object for the tasks table.
 */
@Dao
interface TasksDao {

    /**
     * Observes list of tasks.
     *
     * @return all tasks.
     */
    @Query("select * from Tasks")
    fun observeTasks(): LiveData<List<Task>>

    /**
     * Observes a single task.
     *
     * @param taskId the task id.
     * @return the task with taskId.
     */
    @Query("select * from Tasks where entryid = :taskId")
    fun observeTask(taskId: String): LiveData<Task>

    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     *@param task the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    /**
     * Delete all tasks.
     */
    @Query("delete from Tasks")
    fun deleteTasks()

    /**
     * Delete a task by id.
     *
     * @return the number of tasks deleted. This should always be 1.
     */
    @Query("delete from Tasks where entryid = :taskId")
    fun deleteTaskById(taskId: String): Int

    /**
     * Delete all completed tasks from the table.
     *
     * @return the number of tasks deleted.
     */
    @Query("delete from Tasks where completed = 1")
    fun deleteCompletedTasks(): Int

    /**
     * Update the complete status of a task
     *
     * @param taskId id of the task
     * @param completed status to be updated
     */
    @Query("update Tasks set completed = :completed where entryid = :taskId")
    fun updateCompleted(taskId: String, completed: Boolean)

    /**
     * Select a task by id.
     *
     * @param taskId the task id.
     * @return the task with taskId.
     */
    @Query("select * from Tasks where entryid = :taskId")
    fun getTaskById(taskId: String): Task?

}

