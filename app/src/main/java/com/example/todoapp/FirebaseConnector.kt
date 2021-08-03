package com.example.todoapp

interface FirebaseConnector {
    fun onAllTaskChanged(allTaskData: ArrayList<Task>)
    fun onCompletedTaskChanged(completedTaskData: ArrayList<Task>)
}