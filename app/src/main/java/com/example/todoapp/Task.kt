package com.example.todoapp

import android.os.Parcelable
import java.io.Serializable

class Task(val id: String, val taskName: String, isCompleted: Boolean): Serializable
{
    var isCompleted=isCompleted
        private set

    fun complete()
    {
        isCompleted=true
    }

    fun deComplete()
    {
        isCompleted=false
    }

    override fun equals(other: Any?): Boolean {
        if(taskName==(other as Task).taskName && isCompleted==other.isCompleted)
            return true
        else return false
    }
}