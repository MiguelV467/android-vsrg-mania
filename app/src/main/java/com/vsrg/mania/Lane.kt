package com.vsrg.mania

import android.graphics.RectF

data class Lane(
    val index: Int,
    val bounds: RectF,
    var isPressed: Boolean = false
) {
    fun contains(x: Float): Boolean {
        return x >= bounds.left && x <= bounds.right
    }
}