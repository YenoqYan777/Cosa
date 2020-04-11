package com.example.cosa.arch.helpers

interface SwipeHandler {
    fun onItemSwipedRight(position: Int)
    fun onItemSwipedLeft(position: Int)
}