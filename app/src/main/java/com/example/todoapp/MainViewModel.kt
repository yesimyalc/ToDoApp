package com.example.todoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MainViewModel(state: SavedStateHandle): ViewModel(), FirebaseConnector
{
    companion object{
        const val SECTION_KEY="sectionKey"
        const val COMPLETEDT_KEY="completedTKey"
        const val ALLT_KEY="allTKey"
        const val TEXT_KEY="textKey"

        const val COMPLETED="COMPLETED"
        const val ALL="ALL"
    }
    private val taskRepository=MainRepository(this)

    private val allTasks:MutableLiveData<ArrayList<Task>> = state.getLiveData(ALLT_KEY)
    fun getAllTasks(): LiveData<ArrayList<Task>> {
        return allTasks
    }

    private val completedTasks:MutableLiveData<ArrayList<Task>> = state.getLiveData(COMPLETEDT_KEY)
    fun getCompletedTasks():LiveData<ArrayList<Task>> {
        return completedTasks
    }

    private val taskSection:MutableLiveData<String> = state.getLiveData(SECTION_KEY)
    fun getTaskSection():LiveData<String> {
        return taskSection
    }

    private val typedText:MutableLiveData<String> = state.getLiveData(TEXT_KEY)
    fun getTypedText():LiveData<String> {
        return typedText
    }

    init{
        if(typedText.value==null)
            setTypedText("")

        if(taskSection.value==null)
            taskSection.value=ALL

        if(allTasks.value==null) {
            allTasks.value = ArrayList()
            taskRepository.getAllTasksData()
        }
        if(completedTasks.value==null) {
            completedTasks.value = ArrayList()
            taskRepository.getCompletedTasksData()
        }
    }

    fun setTypedText(newText: String)
    {
        typedText.value=newText
    }

    fun clearAll()
    {
        if(taskSection.value==ALL)
            allTasks.value = ArrayList()
        else
            completedTasks.value = ArrayList()
        taskRepository.clearTasks(taskSection.value!!)
    }

    fun deleteTask(removingTask: Task)
    {
        if (taskSection.value == ALL)
        {
            val newAllTasks = allTasks.value

            newAllTasks!!.remove(removingTask)
            allTasks.value = newAllTasks
        }
        else
        {
            val newCompletedTasks=completedTasks.value

            newCompletedTasks!!.remove(removingTask)
            completedTasks.value =newCompletedTasks
        }
        taskRepository.deleteTask(removingTask, taskSection.value!!)
    }

    fun addNewTask(addingTask: Task)
    {
        val newAllTasks=allTasks.value
        newAllTasks!!.add(addingTask)
        allTasks.value = newAllTasks
        taskRepository.addTask(addingTask)
    }

    fun checkTask(checkingTask: Task)
    {
        val newAllTasks=allTasks.value
        val newCompletedTasks=completedTasks.value

        if(taskSection.value == ALL)
        {
            newAllTasks!!.remove(checkingTask)
            allTasks.value =newAllTasks
            checkingTask.complete()
            newCompletedTasks!!.add(checkingTask)
            completedTasks.value = newCompletedTasks
        }
        else
        {
            newCompletedTasks!!.remove(checkingTask)
            completedTasks.value =newCompletedTasks
            checkingTask.deComplete()
            newAllTasks!!.add(checkingTask)
            allTasks.value =newAllTasks
        }
        taskRepository.checkTask(checkingTask, taskSection.value!!)
    }

    fun changeSection()
    {
        if(taskSection.value==ALL)
            taskSection.value= COMPLETED
        else
            taskSection.value= ALL
    }

    override fun onAllTaskChanged(allTaskData: ArrayList<Task>) {
        allTasks.value = allTaskData
    }

    override fun onCompletedTaskChanged(completedTaskData: ArrayList<Task>) {
        completedTasks.value =completedTaskData
    }
}