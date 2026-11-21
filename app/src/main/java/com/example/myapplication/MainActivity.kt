package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var repository: TaskRepository
    private lateinit var adapter: TaskAdapter
    private lateinit var summary: TextView
    private lateinit var emptyView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repository = TaskRepository(this)
        summary = findViewById(R.id.tasksSummary)
        emptyView = findViewById(R.id.emptyTasks)
        adapter = TaskAdapter(this) { task, checked ->
            repository.setCompleted(task.id, checked)
            refreshTasks()
        }
        findViewById<ListView>(R.id.taskList).apply {
            adapter = this@MainActivity.adapter
            emptyView = this@MainActivity.emptyView
            setOnItemClickListener { _, _, position, _ ->
                val task = this@MainActivity.adapter.getItem(position)
                startActivity(Intent(this@MainActivity, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_TASK_ID, task.id)
                })
            }
        }
        findViewById<Button>(R.id.addTaskButton).setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), REQUEST_ADD_TASK)
        }
    }

    @Deprecated("Activity result API is sufficient for this small three-screen app")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_TASK && resultCode == RESULT_OK &&
            data?.hasExtra(DetailActivity.EXTRA_TASK_ID) == true
        ) {
            refreshTasks()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshTasks()
    }

    private fun refreshTasks() {
        val tasks = repository.all()
        adapter.submitList(tasks)
        summary.text = getString(
            R.string.tasks_summary,
            tasks.count { !it.completed },
            tasks.count { it.completed },
        )
    }

    override fun onDestroy() {
        repository.close()
        super.onDestroy()
    }

    companion object {
        private const val REQUEST_ADD_TASK = 1
    }
}
