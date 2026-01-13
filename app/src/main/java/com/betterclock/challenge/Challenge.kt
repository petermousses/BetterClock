package com.betterclock.challenge

import com.betterclock.data.model.ChallengeDifficulty
import com.betterclock.data.model.ChallengeType

/**
 * Represents a challenge that must be completed to snooze/stop an alarm
 */
sealed class Challenge {
    abstract val type: ChallengeType
    abstract val difficulty: ChallengeDifficulty
    abstract fun checkAnswer(answer: String): Boolean
    abstract fun getDisplayQuestion(): String
    abstract fun getCorrectAnswer(): String
}

/**
 * Math challenge with arithmetic problems
 */
data class MathChallenge(
    override val difficulty: ChallengeDifficulty,
    val operand1: Int,
    val operand2: Int,
    val operator: MathOperator,
    val answer: Int
) : Challenge() {

    override val type: ChallengeType = ChallengeType.MATH

    override fun checkAnswer(answer: String): Boolean {
        return answer.trim().toIntOrNull() == this.answer
    }

    override fun getDisplayQuestion(): String {
        val opSymbol = when (operator) {
            MathOperator.ADD -> "+"
            MathOperator.SUBTRACT -> "-"
            MathOperator.MULTIPLY -> "×"
            MathOperator.DIVIDE -> "÷"
        }
        return "$operand1 $opSymbol $operand2 = ?"
    }

    override fun getCorrectAnswer(): String = answer.toString()
}

enum class MathOperator {
    ADD, SUBTRACT, MULTIPLY, DIVIDE
}

/**
 * Memory challenge - remember and type back a sequence
 */
data class MemoryChallenge(
    override val difficulty: ChallengeDifficulty,
    val sequence: String
) : Challenge() {

    override val type: ChallengeType = ChallengeType.MEMORY

    override fun checkAnswer(answer: String): Boolean {
        return answer.trim().equals(sequence, ignoreCase = true)
    }

    override fun getDisplayQuestion(): String {
        return "Type this sequence:\n$sequence"
    }

    override fun getCorrectAnswer(): String = sequence
}

/**
 * Pattern challenge - complete the pattern
 */
data class PatternChallenge(
    override val difficulty: ChallengeDifficulty,
    val pattern: List<Int>,
    val missingIndex: Int,
    val answer: Int
) : Challenge() {

    override val type: ChallengeType = ChallengeType.PATTERN

    override fun checkAnswer(answer: String): Boolean {
        return answer.trim().toIntOrNull() == this.answer
    }

    override fun getDisplayQuestion(): String {
        val displayPattern = pattern.mapIndexed { index, num ->
            if (index == missingIndex) "?" else num.toString()
        }
        return "Complete the pattern:\n${displayPattern.joinToString(", ")}"
    }

    override fun getCorrectAnswer(): String = answer.toString()
}
