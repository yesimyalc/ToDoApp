package com.example.todoapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity(), TaskViewConnector {

    private val tasksViewModel: MainViewModel by viewModels()
    private var allTasksAdapter: TasksAdapter? = null
    private var completedTasksAdapter: TasksAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newTaskAdd = findViewById<EditText>(R.id.newTaskAdd)

        if(tasksViewModel.getTypedText().value!="")
            newTaskAdd.setText(tasksViewModel.getTypedText().value)

        if(tasksViewModel.getTaskSection().value!=MainViewModel.ALL)
        {
            tasksViewModel.changeSection()
            switchSection()
        }

        newTaskAdd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tasksViewModel.setTypedText(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        tasksViewModel.getAllTasks().observe(this, { allTasks->
            allTasksAdapter?.submitItems(allTasks)
            allTasksAdapter?.notifyDataSetChanged()
        })

        tasksViewModel.getCompletedTasks().observe(this, { completedTasks->
            completedTasksAdapter?.submitItems(completedTasks)
            completedTasksAdapter?.notifyDataSetChanged()
        })

        setUpRecycleViews()
    }

    private fun setUpRecycleViews() {
        val allTasks = findViewById<RecyclerView>(R.id.allTasks)
        allTasks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        allTasksAdapter = TasksAdapter(this, this)
        allTasksAdapter?.submitItems(tasksViewModel.getAllTasks().value!!)
        allTasks.adapter = allTasksAdapter

        val completedTasks = findViewById<RecyclerView>(R.id.completedTasks)
        completedTasks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        completedTasksAdapter = TasksAdapter(this, this)
        completedTasksAdapter?.submitItems(tasksViewModel.getCompletedTasks().value!!)
        completedTasks.adapter = completedTasksAdapter
    }

    fun onAllTasks(view: View) {
        switchSection()
    }

    fun onCompletedTasks(view: View) {
        switchSection()
    }

    private fun switchSection()
    {
        val allTasks = findViewById<RecyclerView>(R.id.allTasks)
        val completedTasks = findViewById<RecyclerView>(R.id.completedTasks)
        val addTaskBar = findViewById<LinearLayout>(R.id.taskAddBar)
        if(tasksViewModel.getTaskSection().value==MainViewModel.ALL)
        {
            addTaskBar.visibility = View.GONE
            allTasks.visibility = View.GONE
            completedTasks.visibility = View.VISIBLE
        }
        else
        {
            addTaskBar.visibility = View.VISIBLE
            allTasks.visibility = View.VISIBLE
            completedTasks.visibility = View.GONE
        }
        tasksViewModel.changeSection()
    }

    fun onCreateTask(view: View) {
        val newTaskAdd = findViewById<EditText>(R.id.newTaskAdd)

        if (newTaskAdd.text.isNotEmpty()) {
            tasksViewModel.addNewTask(Task(UUID.randomUUID().toString(), newTaskAdd.text.toString(), false))
            newTaskAdd.clearFocus()
            newTaskAdd.setText("")
            tasksViewModel.setTypedText("")
            view.requestFocus()
        }
    }

    override fun onCheckBoxClick(task: Task) {
        tasksViewModel.checkTask(task)
    }

    override fun onTrash(task: Task) {
        tasksViewModel.deleteTask(task)
    }

    fun deleteAll(view: View) {
        tasksViewModel.clearAll()
    }

}