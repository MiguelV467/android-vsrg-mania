package com.vsrg.mania

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    private var gameThread: GameThread? = null
    private var gameEngine: GameEngine? = null
    private var beatmap: Beatmap? = null
    
    private val paint = Paint().apply {
        isAntiAlias = true
    }
    
    init {
        holder.addCallback(this)
        isFocusable = true
    }
    
    fun loadBeatmap(beatmap: Beatmap) {
        this.beatmap = beatmap
        gameEngine = GameEngine(beatmap, width, height)
    }
    
    fun isBeatmapLoaded(): Boolean = beatmap != null
    
    fun startGame() {
        gameEngine?.start()
    }
    
    fun pause() {
        gameThread?.pause()
        gameEngine?.pause()
    }
    
    fun resume() {
        gameThread?.resume()
        gameEngine?.resume()
    }
    
    override fun surfaceCreated(holder: SurfaceHolder) {
        gameThread = GameThread(holder)
        gameThread?.setRunning(true)
        gameThread?.start()
    }
    
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        gameEngine?.updateDimensions(width, height)
    }
    
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        gameThread?.setRunning(false)
        while (retry) {
            try {
                gameThread?.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val index = event.actionIndex
                val x = event.getX(index)
                gameEngine?.handleTouch(x, true)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                val index = event.actionIndex
                val x = event.getX(index)
                gameEngine?.handleTouch(x, false)
            }
        }
        return true
    }
    
    private fun draw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        gameEngine?.draw(canvas, paint)
    }
    
    inner class GameThread(private val surfaceHolder: SurfaceHolder) : Thread() {
        private var running = false
        private var paused = false
        
        fun setRunning(running: Boolean) {
            this.running = running
        }
        
        fun pause() {
            paused = true
        }
        
        fun resume() {
            paused = false
        }
        
        override fun run() {
            var canvas: Canvas?
            while (running) {
                if (!paused) {
                    canvas = null
                    try {
                        canvas = surfaceHolder.lockCanvas()
                        synchronized(surfaceHolder) {
                            gameEngine?.update()
                            canvas?.let { draw(it) }
                        }
                    } finally {
                        canvas?.let {
                            surfaceHolder.unlockCanvasAndPost(it)
                        }
                    }
                }
                
                try {
                    sleep(16) // ~60 FPS
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }
}