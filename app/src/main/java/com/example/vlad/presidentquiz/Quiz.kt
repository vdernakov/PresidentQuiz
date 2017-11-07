package com.example.vlad.presidentquiz

/**
 * Created by vlad on 07/11/17.
 */
class Quiz(val numQuestions: Int, val numAnswers: Int, val questions: ArrayList<Question>, val listener: QuizListener) {
    var numUserAnswers = 0
    var result = 0

    fun regiserAnswer(question: Question, userAnswerId: Int, isCheat: Boolean) {
        numUserAnswers++
        if (question.answerId == userAnswerId) result++
        if (numUserAnswers == numQuestions) listener.quizCompleted(result)
    }

    interface QuizListener {
        fun quizCompleted(result: Int)
    }
}