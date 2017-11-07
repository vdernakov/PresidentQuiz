package com.example.vlad.presidentquiz

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by vlad on 07/11/17.
 */
class Answer(val id: Int, val text: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel?, i: Int) {
        parcel!!.writeInt(id)
        parcel!!.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Answer> {
        override fun createFromParcel(parcel: Parcel): Answer {
            return Answer(parcel)
        }

        override fun newArray(size: Int): Array<Answer?> {
            return arrayOfNulls(size)
        }
    }
}