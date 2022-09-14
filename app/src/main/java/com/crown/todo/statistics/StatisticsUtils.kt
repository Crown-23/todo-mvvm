package com.crown.todo.statistics

import com.crown.todo.data.Task

/**
 * Description：Function that does some trivial computation. Used to showcase unit tests.
 */
fun getActiveAndCompletedStats(tasks: List<Task>): StatsResult {
    return if (tasks.isEmpty()) {
        StatsResult(0f, 0f)
    } else {
        val totalTasks = tasks.size
        val numberOfActiveTasks = tasks.count { it.isActive }
        return StatsResult(
            100f * numberOfActiveTasks / totalTasks,
            100f * (totalTasks - numberOfActiveTasks) / totalTasks
        )
    }
}

data class StatsResult(val activeTasksPercent: Float, val completedTasksPercent: Float)