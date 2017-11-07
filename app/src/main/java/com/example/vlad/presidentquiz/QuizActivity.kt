package com.example.vlad.presidentquiz

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class QuizActivity : AppCompatActivity() {
    private var quiz: Quiz? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        setSupportActionBar(toolbar)

        val adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return QuestionFragment.newInstance(
                        quiz!!.questions.get(position),
                        position
                )
            }

            override fun getCount(): Int {
                return quiz!!.questions.size
            }

        }

        async(UI) {
            quiz = QuizFactory.new(10, 4).await()
            viewPager.adapter = adapter
            Log.d("myLog", quiz.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_quiz, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
