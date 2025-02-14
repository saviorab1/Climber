package com.example.climber

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var score = 0
    private lateinit var scoreText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        scoreText = findViewById(R.id.scoreText)
        val climbButton = findViewById<Button>(R.id.climb)
        val fallButton = findViewById<Button>(R.id.fall)
        val resetButton = findViewById<Button>(R.id.reset)

        // Score increase when the Climb button is clicked
        climbButton.setOnClickListener {
            score += 1
            updateScore()
        }

        // Score decrease by 3 when the Fall button is clicked
        fallButton.setOnClickListener {
            score -= 3
            updateScore()
        }

        // Score reset to 0 when the Reset button is clicked
        resetButton.setOnClickListener {
            score = 0
            updateScore()
        }
    }

    // Function to update Score
    private fun updateScore() {
        scoreText.text = "Score: $score"
    }
}