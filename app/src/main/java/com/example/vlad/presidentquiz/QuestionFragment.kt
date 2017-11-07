package com.example.vlad.presidentquiz

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.fragment_question.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

/**
 * Created by vlad on 07/11/17.
 */
class QuestionFragment : Fragment() {
    private var question: Question? = null
    private var questionNumber: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            question = arguments.getParcelable(ARG_QUESTION)
            questionNumber = arguments.getInt(ARG_QUESTION_NUMBER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_question, container, false)
        async(UI) {
            val bitmap = WebServiceApi.loadImage(question!!.image).await()
            view.imageView.setImageBitmap(bitmap)
            val adapter = AnswersAdapter(question!!.answers)
            view.recyclerView.layoutManager = GridLayoutManager(activity, 2)
            view.recyclerView.adapter = adapter
        }
        return view
    }

    companion object {
        private val ARG_QUESTION = "arg_question"
        private val ARG_QUESTION_NUMBER = "arg_questionNumber"

        fun newInstance(question: Question, questionNumber: Int): QuestionFragment {
            val fragment = QuestionFragment()
            val args = Bundle()
            args.putParcelable(ARG_QUESTION, question)
            args.putInt(ARG_QUESTION_NUMBER, questionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}