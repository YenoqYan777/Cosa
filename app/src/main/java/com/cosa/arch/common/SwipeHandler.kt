package com.cosa.arch.common

interface SwipeHandler {
    fun onItemSwipedRight(position: Int)
    fun onItemSwipedLeft(position: Int)
}