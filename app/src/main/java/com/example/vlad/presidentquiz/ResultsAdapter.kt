package com.example.vlad.presidentquiz

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.result_item.view.*

/**
 * Created by vlad on 08/11/17.
 */
class ResultsAdapter(private val results: ArrayList<Result>)
    : RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.numberTextView.text = (position + 1).toString() + "."
        holder.resultTextView.text = result.value.toString()
        holder.nameTextView.text = result.name
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.result_item, parent, false)
        return ViewHolder(
                view,
                view.resultTextView as TextView,
                view.nameTextView as TextView,
                view.numberTextView as TextView)
    }

    override fun getItemCount(): Int {
        return results.size
    }

    class ViewHolder(
            val view: View,
            val resultTextView: TextView,
            val nameTextView: TextView,
            val numberTextView: TextView
    )
        : RecyclerView.ViewHolder(view)

}