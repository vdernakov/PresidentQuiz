package com.example.vlad.presidentquiz

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by vlad on 07/11/17.
 */
class Question(val image: String, val answerId: Int, var answers: ArrayList<Answer>) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readArrayList(Answer.javaClass.classLoader) as ArrayList<Answer>)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeInt(answerId)
        parcel.writeList(answers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}