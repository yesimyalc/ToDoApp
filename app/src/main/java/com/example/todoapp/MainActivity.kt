package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), TaskViewConnector {
    val allTasksList = ArrayList<Task>()
    val completedTasksList = ArrayList<Task>()
    private var tasksAdapter: TasksAdapter? = null
    private var tasksAdapter2: TasksAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpTasks()
    }

    fun onCreateTask(view: View) {
        val newTaskAdd = findViewById<EditText>(R.id.newTaskAdd)

        if (!newTaskAdd.text.isEmpty()) {
            val newTask = Task(newTaskAdd.text.toString(), false)
            allTasksList.add(newTask)
            tasksAdapter!!.notifyDataSetChanged()
            newTaskAdd.clearFocus()
            newTaskAdd.setText("")
            view.requestFocus()
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

        } else if (completedTasksList.contains(task)) {
            completedTasksList.remove(task)
            task.deComplete()
            allTasksList.add(task)
        }
        tasksAdapter?.submitItems(allTasksList)
        tasksAdapter2?.submitItems(completedTasksList)
        tasksAdapter?.notifyDataSetChanged()
        tasksAdapter2?.notifyDataSetChanged()

    }

    override fun onTrash(task: Task) {
        if (allTasksList.contains(task)) {
            allTasksList.remove(task)
        } else if (completedTasksList.contains(task)) {
            completedTasksList.remove(task)
        }
        tasksAdapter?.submitItems(allTasksList)
        tasksAdapter2?.submitItems(completedTasksList)
        tasksAdapter?.notifyDataSetChanged()
        tasksAdapter2?.notifyDataSetChanged()
    }

}