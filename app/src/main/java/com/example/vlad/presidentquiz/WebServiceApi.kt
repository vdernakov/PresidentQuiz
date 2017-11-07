package com.example.vlad.presidentquiz

import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.experimental.async
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.net.URL

/**
 * Created by vlad on 07/11/17.
 */
object WebServiceApi {
    fun readQuestions(numQuestions: Int, numAnswers: Int) = async {
        var questions: ArrayList<Question>? = null

        val data = JSONObject()
        data.put("numQuestions", numQuestions)
        data.put("numAnswers", numAnswers)

        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val client = OkHttpClient()
        try {
            val body = RequestBody.create(mediaType, data.toString())
            val request = Request.Builder()
                    .url(WebServiceConfig.url + WebServiceConfig.apiPath + "readQuiz")
                    .post(body)
                    .build()
            val response = client.newCall(request).execute()
            questions = DataParser.parseQuestions(response.body()!!.string())

        } catch (exc: Exception) {
            Log.d("myLog", exc.toString())
        }
        Log.d("myLog", questions.toString())
        questions
    }

    fun loadImage(name: String) = async {
        var urlString = WebServiceConfig.url + WebServiceConfig.pathToImages + name
        val url = URL(urlString)
        val stream = url.openStream()
        BitmapFactory.decodeStream(stream)
    }
}