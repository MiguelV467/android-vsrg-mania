package com.vsrg.mania

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class GameEngine(private val beatmap: Beatmap, width: Int, height: Int) {
    
    private val lanes = mutableListOf<Lane>()
    private var gameWidth = width
    private var gameHeight = height
    
    private var isPlaying = false
    private var isPaused = false
    private var startTime = 0L
    private var currentTime = 0L
    
    private val scrollSpeed = 0.5f // Notes per millisecond
    private val hitLineY = 0.85f // 85% down the screen
    private val judgementWindow = 100L // ms
    
    private var score = 0
    private var combo = 0
    private var maxCombo = 0
    
    init {
        setupLanes()
    }
    
    private fun setupLanes() {
        lanes.clear()
        val laneWidth = gameWidth.toFloat() / beatmap.keyCount
        
        for (i in 0 until beatmap.keyCount) {
            val left = i * laneWidth
            val right = left + laneWidth
            val bounds = RectF(left, 0f, right, gameHeight.toFloat())
            lanes.add(Lane(i, bounds))
        }
    }
    
    fun updateDimensions(width: Int, height: Int) {
        gameWidth = width
        gameHeight = height
        setupLanes()
    }
    
    fun start() {
        isPlaying = true
        startTime = System.currentTimeMillis()
    }
    
    fun pause() {
        isPaused = true
    }
    
    fun resume() {
        isPaused = false
    }
    
    fun update() {
        if (!isPlaying || isPaused) return
        
        currentTime = System.currentTimeMillis() - startTime
        
        // Check for missed notes
        for (note in beatmap.notes) {
            if (!note.hit && !note.missed && currentTime > note.startTime + judgementWindow) {
                note.missed = true
                combo = 0
            }
        }
    }
    
    fun handleTouch(x: Float, isDown: Boolean) {
        if (!isPlaying) return
        
        for (lane in lanes) {
            if (lane.contains(x)) {
                lane.isPressed = isDown
                
                if (isDown) {
                    checkHit(lane.index)
                }
                break
            }
        }
    }
    
    private fun checkHit(laneIndex: Int) {
        val hitWindow = 100L
        
        for (note in beatmap.notes) {
            if (note.lane == laneIndex && !note.hit && !note.missed) {
                val timeDiff = Math.abs(currentTime - note.startTime)
                
                if (timeDiff <= hitWindow) {
                    note.hit = true
                    score += 300
                    combo++
                    if (combo > maxCombo) maxCombo = combo
                    break
                }
            }
        }
    }
    
    fun draw(canvas: Canvas, paint: Paint) {
        if (gameWidth == 0 || gameHeight == 0) return
        
        // Draw lanes
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = Color.WHITE
        
        for (lane in lanes) {
            canvas.drawRect(lane.bounds, paint)
            
            // Highlight pressed lanes
            if (lane.isPressed) {
                paint.style = Paint.Style.FILL
                paint.color = Color.argb(100, 255, 255, 255)
                canvas.drawRect(lane.bounds, paint)
                paint.style = Paint.Style.STROKE
                paint.color = Color.WHITE
            }
        }
        
        // Draw hit line
        paint.strokeWidth = 4f
        paint.color = Color.GREEN
        val hitLineYPos = gameHeight * hitLineY
        canvas.drawLine(0f, hitLineYPos, gameWidth.toFloat(), hitLineYPos, paint)
        
        // Draw notes
        paint.style = Paint.Style.FILL
        val noteHeight = 20f
        
        for (note in beatmap.notes) {
            if (note.hit || note.missed) continue
            
            val timeDiff = note.startTime - currentTime
            val noteY = hitLineYPos - (timeDiff * scrollSpeed)
            
            if (noteY < -noteHeight || noteY > gameHeight) continue
            
            val lane = lanes[note.lane]
            
            if (note.isLongNote) {
                // Draw long note
                val endTimeDiff = note.endTime - currentTime
                val endY = hitLineYPos - (endTimeDiff * scrollSpeed)
                paint.color = Color.YELLOW
                canvas.drawRect(
                    lane.bounds.left + 5,
                    endY,
                    lane.bounds.right - 5,
                    noteY,
                    paint
                )
            } else {
                // Draw regular note
                paint.color = Color.CYAN
                canvas.drawRect(
                    lane.bounds.left + 5,
                    noteY - noteHeight,
                    lane.bounds.right - 5,
                    noteY,
                    paint
                )
            }
        }
        
        // Draw score and combo
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        paint.textSize = 40f
        canvas.drawText("Score: $score", 20f, 60f, paint)
        canvas.drawText("Combo: $combo", 20f, 110f, paint)
    }
}