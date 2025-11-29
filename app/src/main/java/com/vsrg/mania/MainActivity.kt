package com.vsrg.mania

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    private lateinit var btnSelectBeatmap: Button
    private lateinit var btnStart: Button
    
    private val STORAGE_PERMISSION_CODE = 100
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        gameView = findViewById(R.id.gameView)
        btnSelectBeatmap = findViewById(R.id.btnSelectBeatmap)
        btnStart = findViewById(R.id.btnStart)
        
        checkStoragePermission()
        
        btnSelectBeatmap.setOnClickListener {
            selectBeatmap()
        }
        
        btnStart.setOnClickListener {
            startGame()
        }
    }
    
    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }
    
    private fun selectBeatmap() {
        // TODO: Implement file picker for .osu files
        // For now, use a sample path
        val samplePath = "/storage/emulated/0/osu/Songs/sample/sample.osu"
        loadBeatmap(samplePath)
    }
    
    private fun loadBeatmap(path: String) {
        try {
            val file = File(path)
            if (file.exists()) {
                val parser = OsuBeatmapParser()
                val beatmap = parser.parse(file)
                gameView.loadBeatmap(beatmap)
                Toast.makeText(this, "Beatmap loaded: ${beatmap.title}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Beatmap file not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading beatmap: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun startGame() {
        if (gameView.isBeatmapLoaded()) {
            gameView.startGame()
        } else {
            Toast.makeText(this, "Please select a beatmap first", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onPause() {
        super.onPause()
        gameView.pause()
    }
    
    override fun onResume() {
        super.onResume()
        gameView.resume()
    }
}