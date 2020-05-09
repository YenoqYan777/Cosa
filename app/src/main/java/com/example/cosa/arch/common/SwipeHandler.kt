package com.example.cosa.arch.common

interface SwipeHandler {
    fun onItemSwipedRight(position: Int)
    fun onItemSwipedLeft(position: Int)
}