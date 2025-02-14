package com.example.climber

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val KEY_CURRENT_HOLD = "current_hold"
    private val KEY_HAS_FALLEN = "has_fallen"
    
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

        // Restore state after rotation
        savedInstanceState?.let { bundle ->
            currentHold = bundle.getInt(KEY_CURRENT_HOLD, 0)
            hasFallen = bundle.getBoolean(KEY_HAS_FALLEN, false)
            score = calculateScore()
            updateScoreDisplay()
            updateButtonStates()
            updateBackgroundColor()
        }

        // Score increase when the Climb button is clicked
        climbButton.setOnClickListener {
            if (!hasFallen && currentHold < 9) {
                currentHold++
                score = calculateScore()
                updateScoreDisplay()
                updateButtonStates()
                updateBackgroundColor()
            }
        }

        // Score decrease by 3 when the Fall button is clicked
        fallButton.setOnClickListener {
            if (currentHold > 0 && currentHold < 9 && !hasFallen) {
                hasFallen = true
                score = calculateScore()
                //If score less than 3 then reduce it to 0 else just deduct 3 from it
                score = if (score < 3) 0 else score - 3
                updateScoreDisplay()
                updateButtonStates()
                updateBackgroundColor()
            }
        }

        // Score reset to 0 when the Reset button is clicked
        resetButton.setOnClickListener {
            currentHold = 0
            hasFallen = false
            score = 0
            updateScoreDisplay()
            updateButtonStates()
            updateBackgroundColor()
        }

        updateButtonStates()
        updateBackgroundColor()
    }

    // Function to keep the state even after rotation
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_HOLD, currentHold)
        outState.putBoolean(KEY_HAS_FALLEN, hasFallen)
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

    // Output Score
    private fun updateScoreDisplay() {
        scoreText.text = "Score: $score"
    }

    private fun updateBackgroundColor() {
        val color = when (currentHold) {
            0 -> Color.WHITE
            in 1..3 -> Color.rgb(173, 216, 230)  // Light blue
            in 4..6 -> Color.rgb(144, 238, 144)  // Light green
            in 7..9 -> Color.rgb(255, 182, 193)  // Light red
            else -> Color.WHITE
        }
        findViewById<android.view.View>(R.id.main).setBackgroundColor(color)
    }
}