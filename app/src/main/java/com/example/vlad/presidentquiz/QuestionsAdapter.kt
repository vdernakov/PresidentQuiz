package com.example.vlad.presidentquiz

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup

/**
 * Created by vlad on 08/11/17.
 */
class QuestionsAdapter(fm: FragmentManager, val questions: ArrayList<Question>, val listener: QuestionFragment.QuestionFragmentListener) : FragmentStatePagerAdapter(fm) {
    private val fragments = HashMap<Int, QuestionFragment>()
    override fun getItem(position: Int): Fragment {
        fragments[position]?.let {
            return it
        }
        val fragment = QuestionFragment.newInstance(questions[position], position, listener)
        fragments[position] = fragment
        return fragment
    }

    override fun getCount(): Int {
        return questions.size
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val ret = super.instantiateItem(container, position)
        fragments[position] = ret as QuestionFragment
        return ret
    }
}