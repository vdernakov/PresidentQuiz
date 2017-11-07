package com.example.vlad.presidentquiz

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_question.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

/**
 * Created by vlad on 07/11/17.
 */
class QuestionFragment : Fragment(), AnswersAdapter.AnswerListener {
    private var question: Question? = null
    private var questionNumber: Int? = null
    private var userAnswerId: Int? = null
    private var listener: QuestionFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            question = arguments.getParcelable(ARG_QUESTION)
            questionNumber = arguments.getInt(ARG_QUESTION_NUMBER)
        }

        if (savedInstanceState != null) {
            val userAnswerId = savedInstanceState.getInt("userAnswerId")
            if (userAnswerId > 0) this.userAnswerId = userAnswerId
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_question, container, false)

        val adapter = AnswersAdapter(question!!.answers, question!!.answerId, userAnswerId, this)
        view.recyclerView.layoutManager = GridLayoutManager(activity, 2)
        view.recyclerView.adapter = adapter
        async(UI) {
            val bitmap = WebServiceApi.loadImage(question!!.image).await()
            view.imageView.setImageBitmap(bitmap)
        }
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        userAnswerId?.let { outState.putInt("userAnswerId", it) }
    }

    companion object {
        private val ARG_QUESTION = "arg_question"
        private val ARG_QUESTION_NUMBER = "arg_questionNumber"

        fun newInstance(question: Question, questionNumber: Int, listener: QuestionFragmentListener): QuestionFragment {
            val fragment = QuestionFragment()
            fragment.listener = listener
            val args = Bundle()
            args.putParcelable(ARG_QUESTION, question)
            args.putInt(ARG_QUESTION_NUMBER, questionNumber)
            fragment.arguments = args
            return fragment
        }
    }

    override fun register(userAnswerId: Int) {
        this.userAnswerId = userAnswerId
        listener!!.registerQuestion(question, userAnswerId)
    }

    interface QuestionFragmentListener {
        fun registerQuestion(question: Question?, userAnswerId: Int)
    }
}