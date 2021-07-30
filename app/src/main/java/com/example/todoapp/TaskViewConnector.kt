package com.example.todoapp

interface TaskViewConnector {
    fun onCheckBoxClick(task: Task)
    fun onTrash(task: Task)
}