package com.example.myapplication

data class Task(
    val id: Long,
    val description: String,
    val completed: Boolean,
    val createdAt: Long,
)
