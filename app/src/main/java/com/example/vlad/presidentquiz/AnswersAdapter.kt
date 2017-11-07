package com.example.vlad.presidentquiz

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

/**
 * Created by vlad on 07/11/17.
 */
class AnswersAdapter(val answers: ArrayList<Answer>) : RecyclerView.Adapter<AnswersAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.button.text = answers[position].text
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.answer_button, parent, false)
        return ViewHolder(view as Button)
    }

    override fun getItemCount(): Int {
        return answers.size
    }

    class ViewHolder(var button: Button) : RecyclerView.ViewHolder(button) {

    }

}