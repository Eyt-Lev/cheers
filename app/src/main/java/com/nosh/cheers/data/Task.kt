package com.nosh.cheers.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskModel(
    var title:String,
    var description:String,
    var category: String,
    var isFinished : Int = 0,
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0
)
