package com.example.todoapp

class Task(val id: String, val taskName: String, isCompleted: Boolean)
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