package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AddTaskActivity : Activity() {
    private lateinit var repository: TaskRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        repository = TaskRepository(this)
        val description = findViewById<EditText>(R.id.descriptionInput)
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            val text = description.text.toString().trim()
            if (text.isEmpty()) {
                description.error = getString(R.string.description_required)
                return@setOnClickListener
            }
            val id = repository.add(text)
            setResult(RESULT_OK, Intent().putExtra(DetailActivity.EXTRA_TASK_ID, id))
            finish()
        }
        findViewById<Button>(R.id.cancelButton).setOnClickListener { finish() }
    }

    override fun onDestroy() {
        repository.close()
        super.onDestroy()
    }
}
