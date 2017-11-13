package com.example.vlad.presidentquiz

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.EditText

import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.save_result_dialog.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class ResultActivity : AppCompatActivity() {

    private var value: Int? = null
    private var results: ArrayList<Result>? = null
    private var numQuestions: Int? = null
    private var numAnswers: Int? = null

    private var wasSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        value = intent.getIntExtra("result", 0)
        numQuestions = intent.getIntExtra("numQuestions", 0)
        numAnswers = intent.getIntExtra("numAnswers", 0)

        async(UI) {
            results = WebServiceApi.readResults(numQuestions!!, numAnswers!!).await()
            resultsRecyclerView.layoutManager = LinearLayoutManager(this@ResultActivity)
            resultsRecyclerView.adapter = ResultsAdapter(results!!)
        }

        userResultTextView.text = value.toString()

        if (value == 0) saveResultButton.visibility = View.GONE

        saveResultButton.setOnClickListener {
           val builder = AlertDialog.Builder(this)

            val inflater = this.layoutInflater
            val view = inflater.inflate(R.layout.save_result_dialog, null)
            builder.setView(view)

            builder.setPositiveButton("ок", { _, _ ->
                var name = view.nameEditText.text.toString()
                if (name == "") name = "Anonymous"
                async(UI) {
                    val result = WebServiceApi.writeResult(
                            name,
                            value!!,
                            numQuestions!!,
                            numAnswers!!
                    ).await()

                    val results = WebServiceApi.readResults(numQuestions!!, numAnswers!!).await()

                    resultsRecyclerView.adapter = ResultsAdapter(results)

                    saveResultButton.isEnabled = false
                    wasSaved = true
                }
            })
            builder.setNegativeButton("отмена", { _, _ ->
                //
            })

            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        wasSaved = savedInstanceState.getBoolean("wasSaved")
        if (wasSaved) saveResultButton.isEnabled = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("wasSaved", wasSaved)
    }

}
