package com.example.vlad.presidentquiz

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by vlad on 07/11/17.
 */
object DataParser {
    fun parseQuestions(jsonString: String): ArrayList<Question> {
        val questions = ArrayList<Question>()
        val questionsJSON = JSONArray(jsonString)
        for (i in 0 until questionsJSON.length()) {
            val questionJSON = questionsJSON.getJSONObject(i)

            val image = questionJSON.getString("image")
            val answerId = questionJSON.getInt("answer_id")
            val answers = ArrayList<Answer>()

            val answersJSON = questionJSON.getJSONArray("answers")
            for (j in 0 until answersJSON.length()) {
                val answerJSON = answersJSON.getJSONObject(j)
                val id = answerJSON.getInt("id")
                val text = answerJSON.getString("text")
                answers.add(Answer(id, text))
            }

            questions.add(Question(image, answerId, answers))
        }
        return questions
    }
    fun parseResults(jsonString: String): ArrayList<Result> {
        Log.d("myLogResults", jsonString)
        val results = ArrayList<Result>()
        val resultsJSON = JSONArray(jsonString)
        for (i in 0 until resultsJSON.length()) {
            val resultJSON = resultsJSON.getJSONObject(i)

            val name = resultJSON.getString("name")
            val value = resultJSON.getInt("value")

            results.add(Result(value, name))
        }
        return results
    }
    fun parseResult(jsonString: String): Result {
        val resultJSON = JSONObject(jsonString)
        val name = resultJSON.getString("name")
        val value = resultJSON.getInt("value")
        return Result(value, name)
    }
}