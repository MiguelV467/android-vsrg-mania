package com.vsrg.mania

data class Note(
    val lane: Int,
    val startTime: Long,
    val endTime: Long = startTime,
    val isLongNote: Boolean = false,
    var hit: Boolean = false,
    var missed: Boolean = false
) {
    val duration: Long
        get() = if (isLongNote) endTime - startTime else 0L
}