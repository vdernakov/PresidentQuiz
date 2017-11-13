package com.example.vlad.presidentquiz

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import java.util.jar.Manifest


class QuizActivity : AppCompatActivity(), QuestionFragment.QuestionFragmentListener, Quiz.QuizListener {
    private var quiz: Quiz? = null
    private var settingsChanged = true
    private var hideCheat = false
    private var shareCheat = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        setSupportActionBar(toolbar)

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceListener)

        halfAnswersHelpButton.setOnClickListener({
            helpMenu.close(true)
            hideCheat = true
            halfAnswersHelpButton.isEnabled = false
            hildeHalfOfAnswersHelp()
        })

        shareHelpButton.setOnClickListener({
            val hasPermissions = requestPermissions()
            if (!hasPermissions) return@setOnClickListener
            helpMenu.close(true)
            shareCheat = true
            shareHelpButton.isEnabled = false
            shareHelp()
        })

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                val adapter = viewPager.adapter as QuestionsAdapter
                val currentFragment = adapter.getItem(viewPager.currentItem) as QuestionFragment

                if (currentFragment.userAnswerId != null) {
                    halfAnswersHelpButton.isEnabled = false
                    shareHelpButton.isEnabled = false
                } else {
                    if(!hideCheat) {
                        halfAnswersHelpButton.isEnabled = true
                    }
                    if (!shareCheat) {
                        shareHelpButton.isEnabled = true
                    }
                }
                helpMenu.close(true)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (settingsChanged) {
            settingsChanged = false
            hideCheat = false
            halfAnswersHelpButton.isEnabled = true
            shareCheat = false
            shareHelpButton.isEnabled = true
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

            val numQuestions = sharedPreferences.getString("pref_numQuestions", null).toInt()
            val numAnswers = sharedPreferences.getString("pref_numAnswers", null).toInt()

            viewPager.visibility = View.INVISIBLE
            helpMenu.visibility = View.INVISIBLE
            quizProgressBar.visibility = View.VISIBLE

            ImageCache.clear()

            async(UI) {
                quiz = QuizFactory.new(numQuestions, numAnswers, this@QuizActivity).await()
                quizProgressBar.visibility = View.INVISIBLE
                viewPager.visibility = View.VISIBLE
                helpMenu.visibility = View.VISIBLE
                viewPager.adapter = QuestionsAdapter(
                        supportFragmentManager,
                        quiz!!.questions,
                        this@QuizActivity)
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        quiz = savedInstanceState.getParcelable("quiz")
        quiz!!.listener = this
        viewPager.adapter = QuestionsAdapter(supportFragmentManager, quiz!!.questions, this@QuizActivity)

        settingsChanged = savedInstanceState.getBoolean("settingsChanged")

        hideCheat = savedInstanceState.getBoolean("hideCheat")
        if (hideCheat) halfAnswersHelpButton.isEnabled = false

        shareCheat = savedInstanceState.getBoolean("shareCheat")
        if (shareCheat) shareHelpButton.isEnabled = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("quiz", quiz)
        outState.putBoolean("settingsChanged", settingsChanged)
        outState.putBoolean("hideCheat", hideCheat)
        outState.putBoolean("shareCheat", shareCheat)
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

    private fun hildeHalfOfAnswersHelp() {
        val adapter = viewPager.adapter as QuestionsAdapter
        val currentFragment = adapter.getItem(viewPager.currentItem) as QuestionFragment

        currentFragment.hideHalfOfAnswers()
    }

    private fun shareHelp() {
        val screen = getScreenShot()

        val name = "quiz_" + System.currentTimeMillis() + ".jpg"
        val location = MediaStore.Images.Media.insertImage(
                this.contentResolver, screen, name,
                name)

        val uri = Uri.parse(location)

        val intent = Intent(Intent.ACTION_SEND, uri)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivityForResult(intent, 1)
    }

    private fun getScreenShot(): Bitmap {
        val screenView = window.decorView.rootView
        screenView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(screenView.drawingCache)
        screenView.isDrawingCacheEnabled = false
        return bitmap
    }

    private val preferenceListener = SharedPreferences.OnSharedPreferenceChangeListener {
        _, _ -> settingsChanged = true
    }

    private fun requestPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            return false
        }
        return true
    }

    private fun dispatchSettingsIntent() {
        val preferencesIntent = Intent(this, SettingsActivity::class.java)
        startActivity(preferencesIntent)
    }

    private fun nextQuestion() {
        val handler = Handler()
        handler.postDelayed({ viewPager.currentItem = viewPager.currentItem + 1 }, 200)
    }

    override fun registerQuestion(question: Question?, userAnswerId: Int) {
        quiz!!.registerAnswer(question!!, userAnswerId)
        helpMenu.close(true)
        nextQuestion()
    }

    override fun quizCompleted(result: Int) {
        settingsChanged = true
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("result", result)
        intent.putExtra("numQuestions", quiz!!.numQuestions)
        intent.putExtra("numAnswers", quiz!!.numAnswers)
        startActivity(intent)
    }
}
