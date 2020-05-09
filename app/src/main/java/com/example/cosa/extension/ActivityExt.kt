package com.example.cosa.extension

import android.app.Activity
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.cosa.R

fun Activity.pushFragment(
    fragment: Fragment, fragmentManager: FragmentManager,
    @IdRes resId: Int, addToBackStack: Boolean
) {
    val transaction: FragmentTransaction = fragmentManager.beginTransaction()
    if (fragmentManager.fragments.contains(fragment)) {
        transaction.replace(resId, fragment)

    } else {
        transaction.add(resId, fragment)
        if (addToBackStack) transaction.addToBackStack(null)
    }

    transaction.commit()
}