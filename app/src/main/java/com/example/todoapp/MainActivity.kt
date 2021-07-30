package com.example.todoapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), TaskViewConnector {
    val allTasksList = ArrayList<Task>()
    val completedTasksList = ArrayList<Task>()
    private var tasksAdapter: TasksAdapter? = null
    private var tasksAdapter2: TasksAdapter? = null
    val allTasksRef:DatabaseReference=FirebaseDatabase.getInstance().getReference().child("allTasks")
    val completedTasksRef:DatabaseReference=FirebaseDatabase.getInstance().getReference().child("completedTasks")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpTasks()

        allTasksRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(allTasksList.isEmpty()) {
                    for (ds in dataSnapshot.children) {
                        var task: String = ds.value as String
                        var taskID: String=ds.key as String
                        allTasksList.add(Task(taskID, task, false))
                    }
                    tasksAdapter?.submitItems(allTasksList)
                    tasksAdapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        completedTasksRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(completedTasksList.isEmpty()) {
                    for (ds in dataSnapshot.children) {
                        var task: String = ds.value as String
                        var taskID: String=ds.key as String
                        completedTasksList.add(Task(taskID, task, true))
                    }
                    tasksAdapter2?.submitItems(completedTasksList)
                    tasksAdapter2?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun onCreateTask(view: View) {
        val newTaskAdd = findViewById<EditText>(R.id.newTaskAdd)

        if (!newTaskAdd.text.isEmpty()) {
            val newUUID = UUID.randomUUID()
            val newTask = Task(newUUID.toString(), newTaskAdd.text.toString(), false)
            allTasksList.add(newTask)
            tasksAdapter!!.notifyDataSetChanged()
            newTaskAdd.clearFocus()
            newTaskAdd.setText("")
            view.requestFocus()
            allTasksRef.child(newTask.id).setValue(newTask.taskName)
        }
    }

    fun setUpTasks() {
        val allTasks = findViewById<RecyclerView>(R.id.allTasks)
        allTasks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tasksAdapter = TasksAdapter(this, this)
        tasksAdapter?.submitItems(allTasksList)
        allTasks.adapter = tasksAdapter

        val completedTasks = findViewById<RecyclerView>(R.id.completedTasks)
        completedTasks.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tasksAdapter2 = TasksAdapter(this, this)
        tasksAdapter2?.submitItems(completedTasksList)
        completedTasks.adapter = tasksAdapter2
    }

    fun onAllTasks(view: View) {
        val allTasks = findViewById<RecyclerView>(R.id.allTasks)
        val completedTasks = findViewById<RecyclerView>(R.id.completedTasks)
        val addTaskBar = findViewById<LinearLayout>(R.id.taskAddBar)
        addTaskBar.visibility = View.VISIBLE
        allTasks.visibility = View.VISIBLE
        completedTasks.visibility = View.GONE
    }

    fun onCompletedTasks(view: View) {
        val allTasks = findViewById<RecyclerView>(R.id.allTasks)
        val completedTasks = findViewById<RecyclerView>(R.id.completedTasks)
        val addTaskBar = findViewById<LinearLayout>(R.id.taskAddBar)
        addTaskBar.visibility = View.GONE
        allTasks.visibility = View.GONE
        completedTasks.visibility = View.VISIBLE
    }

    override fun onCheckBoxClick(task: Task) {
        if (allTasksList.contains(task)) {
            allTasksList.remove(task)
            task.complete()
            completedTasksList.add(task)
            allTasksRef.child(task.id).removeValue()
            completedTasksRef.child(task.id).setValue(task.taskName)

        } else if (completedTasksList.contains(task)) {
            completedTasksList.remove(task)
            task.deComplete()
            allTasksList.add(task)
            completedTasksRef.child(task.id).removeValue()
            allTasksRef.child(task.id).setValue(task.taskName)
        }
        tasksAdapter?.submitItems(allTasksList)
        tasksAdapter2?.submitItems(completedTasksList)
        tasksAdapter?.notifyDataSetChanged()
        tasksAdapter2?.notifyDataSetChanged()

    }

    override fun onTrash(task: Task) {
        if (allTasksList.contains(task)) {
            allTasksList.remove(task)
            allTasksRef.child(task.id).removeValue()
        } else if (completedTasksList.contains(task)) {
            completedTasksList.remove(task)
            completedTasksRef.child(task.id).removeValue()
        }
        tasksAdapter?.submitItems(allTasksList)
        tasksAdapter2?.submitItems(completedTasksList)
        tasksAdapter?.notifyDataSetChanged()
        tasksAdapter2?.notifyDataSetChanged()
    }

    fun deleteAll(view: View)
    {
        val allTasksBar=findViewById<RadioButton>(R.id.allTasksBar)
        if(allTasksBar.isChecked)
        {
            allTasksList.clear()
            allTasksRef.removeValue()
            tasksAdapter?.submitItems(allTasksList)
            tasksAdapter?.notifyDataSetChanged()
        }
        else
        {
            completedTasksList.clear()
            completedTasksRef.removeValue()
            tasksAdapter2?.submitItems(completedTasksList)
            tasksAdapter2?.notifyDataSetChanged()
        }
    }

}