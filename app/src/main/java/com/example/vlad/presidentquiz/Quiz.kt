package com.example.vlad.presidentquiz

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by vlad on 07/11/17.
 */
class Quiz(
        val numQuestions: Int,
        val numAnswers: Int,
        val questions: ArrayList<Question>,
        var listener: QuizListener?) : Parcelable {
    var numUserAnswers = 0
    var result = 0

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readArrayList(Question.javaClass.classLoader) as ArrayList<Question>,
            null) {
        numUserAnswers = parcel.readInt()
        result = parcel.readInt()
    }

    fun registerAnswer(question: Question, userAnswerId: Int) {
        numUserAnswers++
        if (question.answerId == userAnswerId) result += 100
        if (numUserAnswers == numQuestions) listener!!.quizCompleted(result)
    }

    interface QuizListener {
        fun quizCompleted(result: Int)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(numQuestions)
        parcel.writeInt(numAnswers)
        parcel.writeList(questions)
        parcel.writeInt(numUserAnswers)
        parcel.writeInt(result)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Quiz> {
        override fun createFromParcel(parcel: Parcel): Quiz {
            return Quiz(parcel)
        }

        override fun newArray(size: Int): Array<Quiz?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "[Quiz]: numQuestions: ${numQuestions}, numAnswers: ${numAnswers}, numUeserAnswers: ${numUserAnswers}"
    }
}