package com.example.myapplication

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import java.text.DateFormat
import java.util.Date

class TaskAdapter(
    private val context: Context,
    private val onCompletedChange: (Task, Boolean) -> Unit,
) : BaseAdapter() {
    private var tasks: List<Task> = emptyList()

    fun submitList(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    override fun getCount() = tasks.size
    override fun getItem(position: Int) = tasks[position]
    override fun getItemId(position: Int) = tasks[position].id
    override fun hasStableIds() = true

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)
        val task = getItem(position)
        val checkBox = view.findViewById<CheckBox>(R.id.taskCheckBox)
        val description = view.findViewById<TextView>(R.id.taskDescription)
        val meta = view.findViewById<TextView>(R.id.taskMeta)

        checkBox.setOnCheckedChangeListener(null)
        checkBox.isChecked = task.completed
        checkBox.setOnCheckedChangeListener { _, checked -> onCompletedChange(task, checked) }
        description.text = task.description
        description.paintFlags = if (task.completed) {
            description.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            description.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        description.alpha = if (task.completed) 0.55f else 1f
        meta.text = "${context.getString(if (task.completed) R.string.completed else R.string.pending)} · " +
            DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(task.createdAt))
        return view
    }
}
