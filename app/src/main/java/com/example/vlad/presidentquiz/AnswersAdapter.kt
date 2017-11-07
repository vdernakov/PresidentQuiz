package com.example.vlad.presidentquiz

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

/**
 * Created by vlad on 07/11/17.
 */
class AnswersAdapter(
        private val answers: ArrayList<Answer>,
        private val answerId: Int,
        private val userAnswerId: Int?,
        private val listener: AnswerListener)
    : RecyclerView.Adapter<AnswersAdapter.ViewHolder>() {

    private val buttons: ArrayList<Button> = ArrayList()
    private var correctAnswerButton: Button? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val answer = answers[position]

        holder.button.text = answer.text

        if (userAnswerId != null) {
            if (answer.id == userAnswerId) {
                holder.button.setBackgroundColor(Color.RED)
            }
            if (answer.id == answerId) {
                holder.button.setBackgroundColor(Color.GREEN)
            }
            holder.button.isClickable = false
            return
        }

        holder.button.setOnClickListener {
            disableButtons()
            holder.button.setBackgroundColor(if (answerId == answer.id) Color.GREEN else Color.RED)
            correctAnswerButton!!.setBackgroundColor(Color.GREEN)
            listener.register(answer.id)
        }

        if (answer.id == answerId) correctAnswerButton = holder.button

        buttons.add(holder.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.answer_button, parent, false)
        return ViewHolder(view as Button)
    }

    override fun getItemCount(): Int {
        return answers.size
    }

    class ViewHolder(var button: Button) : RecyclerView.ViewHolder(button)

    private fun disableButtons() {
        for (button in buttons) {
            button.isClickable = false
        }
    }

    interface AnswerListener {
        fun register(userAnswerId: Int)
    }

}