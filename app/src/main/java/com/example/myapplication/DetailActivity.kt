package com.example.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import java.text.DateFormat
import java.util.Date

class DetailActivity : Activity() {
    private lateinit var repository: TaskRepository
    private var taskId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        repository = TaskRepository(this)
        taskId = intent.getLongExtra(EXTRA_TASK_ID, -1)

        findViewById<CheckBox>(R.id.detailCompleted).setOnCheckedChangeListener { _, checked ->
            repository.setCompleted(taskId, checked)
            showTask()
        }
        findViewById<Button>(R.id.deleteButton).setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(R.string.delete_confirmation)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete) { _, _ ->
                    repository.delete(taskId)
                    finish()
                }
                .show()
        }
        findViewById<Button>(R.id.backButton).setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        showTask()
    }

    private fun showTask() {
        val task = repository.find(taskId)
        if (task == null) {
            finish()
            return
        }
        findViewById<TextView>(R.id.detailDescription).text = task.description
        findViewById<TextView>(R.id.detailStatus).text =
            getString(if (task.completed) R.string.completed else R.string.pending)
        findViewById<TextView>(R.id.detailCreated).text =
            DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(Date(task.createdAt))
        findViewById<CheckBox>(R.id.detailCompleted).apply {
            setOnCheckedChangeListener(null)
            isChecked = task.completed
            setOnCheckedChangeListener { _, checked ->
                repository.setCompleted(taskId, checked)
                showTask()
            }
        }
    }

    override fun onDestroy() {
        repository.close()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_TASK_ID = "com.example.myapplication.TASK_ID"
    }
}
