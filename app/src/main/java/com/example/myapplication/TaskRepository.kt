package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/** Local storage for tasks. No account or network connection is needed. */
class TaskRepository(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "description TEXT NOT NULL, " +
                "completed INTEGER NOT NULL DEFAULT 0, " +
                "created_at INTEGER NOT NULL)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit

    fun all(): List<Task> = readableDatabase.query(
        "tasks", COLUMNS, null, null, null, null, "created_at DESC, id DESC"
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) add(cursor.toTask())
        }
    }

    fun find(id: Long): Task? = readableDatabase.query(
        "tasks", COLUMNS, "id = ?", arrayOf(id.toString()), null, null, null
    ).use { cursor ->
        if (cursor.moveToFirst()) cursor.toTask() else null
    }

    fun add(description: String): Long {
        val values = ContentValues().apply {
            put("description", description.trim())
            put("completed", 0)
            put("created_at", System.currentTimeMillis())
        }
        return writableDatabase.insertOrThrow("tasks", null, values)
    }

    fun setCompleted(id: Long, completed: Boolean) {
        val values = ContentValues().apply { put("completed", if (completed) 1 else 0) }
        writableDatabase.update("tasks", values, "id = ?", arrayOf(id.toString()))
    }

    fun delete(id: Long) {
        writableDatabase.delete("tasks", "id = ?", arrayOf(id.toString()))
    }

    private fun Cursor.toTask() = Task(
        id = getLong(0),
        description = getString(1),
        completed = getInt(2) != 0,
        createdAt = getLong(3),
    )

    companion object {
        private const val DATABASE_NAME = "tasks.db"
        private val COLUMNS = arrayOf("id", "description", "completed", "created_at")
    }
}
