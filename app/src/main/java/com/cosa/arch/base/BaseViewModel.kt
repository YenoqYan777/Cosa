package com.cosa.arch.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.cosa.util.Event
import com.cosa.util.NavigationCommand

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val mNavigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> = mNavigation

    fun navigate(navDirections: NavDirections) {
        mNavigation.value =
            Event(NavigationCommand.To(navDirections))
    }

    fun navigateBack() {
        mNavigation.value =
            Event(NavigationCommand.Back)
    }
}