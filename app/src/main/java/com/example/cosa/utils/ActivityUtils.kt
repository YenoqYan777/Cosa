package com.example.cosa.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class ActivityUtils {
    fun pushFragment(
        @NonNull fragment: Fragment, @NonNull fragmentManager: FragmentManager,
        @IdRes resId: Int, addToBackStack: Boolean
    ) {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction().remove(fragment)
        if (addToBackStack) {
            transaction.add(resId, fragment)
            transaction.addToBackStack(null)
        } else {
            transaction.replace(resId, fragment)
        }
        transaction.commit()
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
        if (inputMethodManager.isActive) {
            if (activity.currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken, 0
                )
            }
        }
    }
}