package com.example.cosa.arch.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.cosa.arch.MainActivity

abstract class BaseFragment: Fragment()  {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? MainActivity)?.setBarItemChecked(this)
    }
}