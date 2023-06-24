package com.nosh.cheers.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(todoModel: TaskModel):Long

    @Query("Select * from TaskModel where isFinished == 0")
    fun getUnFinishedTasks():LiveData<List<TaskModel>>

    @Query("Select * from TaskModel")
    fun getTasks():LiveData<List<TaskModel>>

    @Query("UPDATE TaskModel SET title = :title, description = :description, category = :category, isFinished = :isFinished WHERE id = :uid")
    fun updateTask(uid: Long, title: String, description: String, category: String, date: Long, time: Long, isFinished: Int)

    @Query("Update TaskModel Set isFinished = 1 where id=:uid")
    fun finishTask(uid:Long)

    @Query("Delete from TaskModel where id=:uid")
    fun deleteTask(uid:Long)
}