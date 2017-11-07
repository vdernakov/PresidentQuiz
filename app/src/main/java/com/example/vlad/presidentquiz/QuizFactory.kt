package com.example.vlad.presidentquiz

import kotlinx.coroutines.experimental.async

/**
 * Created by vlad on 07/11/17.
 */
object QuizFactory {
    fun new(numQuestions: Int, numAnswers: Int, listener: Quiz.QuizListener) = async {
        val answers = WebServiceApi.readQuestions(numQuestions, numAnswers).await()
        val quiz = Quiz(numQuestions, numAnswers, answers!!, listener)
        quiz
    }
}