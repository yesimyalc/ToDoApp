package com.example.todoapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TasksAdapter(val context: Context,val connector: TaskViewConnector): RecyclerView.Adapter<TasksAdapter.ViewHolder>()
{
    private var holder:ViewHolder?=null
    private var items:ArrayList<Task>?=null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val task=view.findViewById<TextView>(R.id.taskText)
        val taskBox=view.findViewById<CheckBox>(R.id.taskCheck)
        val trashBox=view.findViewById<ImageView>(R.id.trashBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        holder=ViewHolder(LayoutInflater.from(context).inflate(R.layout.task_appearance, parent, false))
        return holder!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val currentTask=items?.get(position) ?:return
        holder.taskBox.setOnClickListener{ view ->
            connector.onCheckBoxClick(currentTask)
        }
        holder.trashBox.setOnClickListener{ view->
            connector.onTrash(currentTask)
        }
        holder.task.text=currentTask.taskName
        if(currentTask.isCompleted)
            holder.taskBox.isChecked=true
        else
            holder.taskBox.isChecked=false
    }

    override fun getItemCount(): Int
    {
        return items?.size ?:0
    }

    fun submitItems(items:ArrayList<Task>)
    {
        this.items=items
    }

}