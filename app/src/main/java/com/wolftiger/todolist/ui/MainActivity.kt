@file:Suppress("DEPRECATION")

package com.wolftiger.todolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.wolftiger.todolist.databinding.ActivityMainBinding
import com.wolftiger.todolist.datasource.TaskDateSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()

        insertListeners()
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener{
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
            updateList()
        }

        adapter.listnerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java )
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(Intent(this, AddTaskActivity::class.java ), CREATE_NEW_TASK)
            updateList()
        }

        adapter.listnerDelete = {
            TaskDateSource.delete(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK)
            updateList()
    }

    fun updateList(){
        val list = TaskDateSource.getList()
        adapter.submitList(list)
    }

    companion object{
         private const val CREATE_NEW_TASK = 1000
    }
}