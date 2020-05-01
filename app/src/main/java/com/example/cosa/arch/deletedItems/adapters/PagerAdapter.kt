package com.example.cosa.arch.deletedItems.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cosa.R
import com.example.cosa.arch.deletedItems.DeletedNotesFragment
import com.example.cosa.arch.deletedItems.DeletedThingsFragment

class PagerAdapter(
    fm: FragmentManager,
    context: Context
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val things: String = context.resources.getString(R.string.things);
    private val notes: String = context.resources.getString(R.string.notes);


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                DeletedThingsFragment()
            }
            else -> DeletedNotesFragment()
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> things
            else -> notes
        }
    }
}