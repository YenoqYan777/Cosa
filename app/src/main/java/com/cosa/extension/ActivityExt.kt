package com.cosa.extension

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

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

fun hideKeyboard(activity: Activity) {
    val imm: InputMethodManager =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}