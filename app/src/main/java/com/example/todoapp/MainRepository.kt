package com.example.todoapp

import com.google.firebase.database.*

class MainRepository(val firebaseConnector: FirebaseConnector)
{
    private val allTasksData=ArrayList<Task>()
    private val completedTaskData=ArrayList<Task>()
    private var pastTime:Long=System.currentTimeMillis()

    val allTasksRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("allTasks")
    val completedTasksRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("completedTasks")

    fun getAllTasksData()
    {
        val query: Query = allTasksRef.orderByChild("Add Time")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(allTasksData.isEmpty()) {
                    for (ds in dataSnapshot.children) {
                        val task: String = ds.child("Task Name").value as String
                        val taskID: String=ds.toString()
                        allTasksData.add(Task(taskID, task, false))
                    }
                    firebaseConnector.onAllTaskChanged(allTasksData)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getCompletedTasksData()
    {
        val query: Query=completedTasksRef.orderByChild("Add Time")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(completedTaskData.isEmpty()) {
                    for (ds in dataSnapshot.children) {
                        val task: String = ds.child("Task Name").value as String
                        val taskID: String=ds.toString()
                        completedTaskData.add(Task(taskID, task, true))
                    }
                    firebaseConnector.onCompletedTaskChanged(completedTaskData)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun addTask(newTask: Task)
    {
        allTasksRef.child(newTask.id).child("Task Name").setValue(newTask.taskName)
        allTasksRef.child(newTask.id).child("Add Time").setValue(pastTime)
        pastTime=System.currentTimeMillis()
    }

    fun clearTasks(section: String)
    {
        if(section==MainViewModel.ALL)
            allTasksRef.removeValue()
        else
            completedTasksRef.removeValue()
    }

    fun checkTask(task: Task, section: String)
    {
        pastTime=System.currentTimeMillis()
        if(section==MainViewModel.ALL)
        {
            allTasksRef.child(task.id).removeValue()
            completedTasksRef.child(task.id).child("Task Name").setValue(task.taskName)
            completedTasksRef.child(task.id).child("Add Time").setValue(pastTime)
        }
        else
        {
            completedTasksRef.child(task.id).removeValue()
            allTasksRef.child(task.id).child("Task Name").setValue(task.taskName)
            allTasksRef.child(task.id).child("Add Time").setValue(pastTime)
        }
    }

    fun deleteTask(task: Task, section: String)
    {
        if(section==MainViewModel.ALL)
            allTasksRef.child(task.id).removeValue()
        else
            completedTasksRef.child(task.id).removeValue()
    }

}