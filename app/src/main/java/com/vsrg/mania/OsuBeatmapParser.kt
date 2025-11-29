package com.vsrg.mania

import java.io.File

class OsuBeatmapParser {
    
    fun parse(file: File): Beatmap {
        val beatmap = Beatmap()
        val lines = file.readLines()
        
        var currentSection = ""
        var keyCount = 4 // Default to 4K
        
        for (line in lines) {
            val trimmed = line.trim()
            
            if (trimmed.isEmpty() || trimmed.startsWith("//")) continue
            
            if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
                currentSection = trimmed.substring(1, trimmed.length - 1)
                continue
            }
            
            when (currentSection) {
                "General" -> parseGeneral(trimmed, beatmap)
                "Metadata" -> parseMetadata(trimmed, beatmap)
                "Difficulty" -> parseDifficulty(trimmed, beatmap)
                "HitObjects" -> parseHitObject(trimmed, beatmap, keyCount)
            }
        }
        
        return beatmap
    }
    
    private fun parseGeneral(line: String, beatmap: Beatmap) {
        val parts = line.split(":")
        if (parts.size < 2) return
        
        when (parts[0].trim()) {
            "AudioFilename" -> beatmap.audioFilename = parts[1].trim()
        }
    }
    
    private fun parseMetadata(line: String, beatmap: Beatmap) {
        val parts = line.split(":")
        if (parts.size < 2) return
        
        when (parts[0].trim()) {
            "Title" -> beatmap.title = parts[1].trim()
            "Artist" -> beatmap.artist = parts[1].trim()
            "Creator" -> beatmap.creator = parts[1].trim()
            "Version" -> beatmap.version = parts[1].trim()
        }
    }
    
    private fun parseDifficulty(line: String, beatmap: Beatmap) {
        val parts = line.split(":")
        if (parts.size < 2) return
        
        when (parts[0].trim()) {
            "CircleSize" -> {
                val cs = parts[1].trim().toFloatOrNull() ?: 4f
                beatmap.keyCount = cs.toInt()
            }
        }
    }
    
    private fun parseHitObject(line: String, beatmap: Beatmap, keyCount: Int) {
        val parts = line.split(",")
        if (parts.size < 4) return
        
        val x = parts[0].toIntOrNull() ?: return
        val time = parts[2].toLongOrNull() ?: return
        val type = parts[3].toIntOrNull() ?: return
        
        // Calculate lane from x position (osu!mania specific)
        val lane = (x * keyCount / 512).coerceIn(0, keyCount - 1)
        
        // Check if it's a long note (hold note)
        val isLongNote = (type and 128) > 0
        var endTime = time
        
        if (isLongNote && parts.size > 5) {
            val endTimePart = parts[5].split(":")
            endTime = endTimePart[0].toLongOrNull() ?: time
        }
        
        val note = Note(lane, time, endTime, isLongNote)
        beatmap.notes.add(note)
    }
}

data class Beatmap(
    var title: String = "",
    var artist: String = "",
    var creator: String = "",
    var version: String = "",
    var audioFilename: String = "",
    var keyCount: Int = 4,
    val notes: MutableList<Note> = mutableListOf()
)