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
    private var currentHold = 0
    private var hasFallen = false
    private lateinit var scoreText: TextView
    private lateinit var climbButton: Button
    private lateinit var fallButton: Button

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
        climbButton = findViewById(R.id.climb)
        fallButton = findViewById(R.id.fall)
        val resetButton = findViewById<Button>(R.id.reset)

        // Score increase when the Climb button is clicked
        climbButton.setOnClickListener {
            if (!hasFallen && currentHold < 9) {
                currentHold++
                updateScore()
                updateButtonStates()
            }
        }

        // Score decrease by 3 when the Fall button is clicked
        fallButton.setOnClickListener {
            if (currentHold > 0 && currentHold < 9 && !hasFallen) {
                hasFallen = true
                // Set the condition that if score is less than 3, it becomes 0
                score = if (score < 3) 0 else score - 3
                updateScore()
                updateButtonStates()
            }
        }

        // Score reset to 0 when the Reset button is clicked
        resetButton.setOnClickListener {
            score = 0
            currentHold = 0
            hasFallen = false
            updateScore()
            updateButtonStates()
        }

        updateButtonStates()
    }

    // Function to calculate score based on the current hold
    private fun calculateScore(): Int {
        var totalScore = 0
        for (hold in 1..currentHold) {
            totalScore += when (hold) {
                in 1..3 -> 1  // Blue zone
                in 4..6 -> 2  // Green zone
                in 7..9 -> 3  // Red zone
                else -> 0
            }
        }
        return totalScore
    }

    /* Function to set the button conditions, if Fall button is pushed
    or the current hold less than 9, grey out the Climb button.
    The Fall button is greyed out when it is pushed once or the current hold is not within 0 and 9
     */
    private fun updateButtonStates() {
        climbButton.isEnabled = !hasFallen && currentHold < 9
        fallButton.isEnabled = currentHold > 0 && currentHold < 9 && !hasFallen
    }

    // Function to update Score
    private fun updateScore() {
        score = calculateScore()
        scoreText.text = "Score: $score"
    }
}