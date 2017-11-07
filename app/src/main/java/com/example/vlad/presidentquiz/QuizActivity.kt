package com.example.vlad.presidentquiz

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class QuizActivity : AppCompatActivity(), QuestionFragment.QuestionFragmentListener, Quiz.QuizListener {
    private val PREF_NUM_QUESTIONS = "pref_numQuestions"
    private val PREF_NUM_ANSWERS = "pref_numAnswers"

    private var quiz: Quiz? = null
    private var settingsChanged = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        setSupportActionBar(toolbar)

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceListener)
    }

    override fun onResume() {
        super.onResume()
        Log.d("myLog", "onResume")
        if (settingsChanged) {
            settingsChanged = false
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

            val numQuestions = sharedPreferences.getString("pref_numQuestions", null).toInt()
            val numAnswers = sharedPreferences.getString("pref_numAnswers", null).toInt()

            async(UI) {
                quiz = QuizFactory.new(numQuestions, numAnswers, this@QuizActivity).await()
                viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
                    override fun getItem(position: Int): Fragment {
                        return QuestionFragment.newInstance(
                                quiz!!.questions[position],
                                position,
                                this@QuizActivity
                        )
                    }
                    override fun getCount(): Int {
                        return quiz!!.questions.size
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_quiz, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                dispatchSettingsIntent()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener {
        _, _ -> settingsChanged = true
    }

    private fun dispatchSettingsIntent() {
        val preferencesIntent = Intent(this, SettingsActivity::class.java)
        startActivity(preferencesIntent)
    }

    override fun registerQuestion(question: Question?, userAnswerId: Int) {
        quiz!!.regiserAnswer(question!!, userAnswerId, false)
    }

    override fun quizCompleted(result: Int) {
        settingsChanged = true
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("result", result)
        startActivity(intent)
    }
}
