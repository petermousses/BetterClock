package com.betterclock.challenge

import com.betterclock.data.model.ChallengeDifficulty
import com.betterclock.data.model.ChallengeType
import kotlin.random.Random

/**
 * Generates challenges based on type and difficulty.
 * Challenge generation is designed to complete in <100ms.
 */
object ChallengeGenerator {

    /**
     * Generate a challenge of the specified type and difficulty
     */
    fun generateChallenge(type: ChallengeType, difficulty: ChallengeDifficulty): Challenge {
        return when (type) {
            ChallengeType.MATH -> generateMathChallenge(difficulty)
            ChallengeType.MEMORY -> generateMemoryChallenge(difficulty)
            ChallengeType.PATTERN -> generatePatternChallenge(difficulty)
            ChallengeType.NONE -> throw IllegalArgumentException("Cannot generate NONE challenge type")
        }
    }

    /**
     * Generate a math challenge with appropriate difficulty
     */
    fun generateMathChallenge(difficulty: ChallengeDifficulty): MathChallenge {
        return when (difficulty) {
            ChallengeDifficulty.EASY -> generateEasyMathChallenge()
            ChallengeDifficulty.MEDIUM -> generateMediumMathChallenge()
            ChallengeDifficulty.HARD -> generateHardMathChallenge()
        }
    }

    private fun generateEasyMathChallenge(): MathChallenge {
        // Easy: Single digit addition/subtraction
        val operator = if (Random.nextBoolean()) MathOperator.ADD else MathOperator.SUBTRACT
        val operand1 = Random.nextInt(1, 10)
        val operand2 = Random.nextInt(1, 10)

        return when (operator) {
            MathOperator.ADD -> MathChallenge(
                difficulty = ChallengeDifficulty.EASY,
                operand1 = operand1,
                operand2 = operand2,
                operator = operator,
                answer = operand1 + operand2
            )
            MathOperator.SUBTRACT -> {
                // Ensure positive result
                val (a, b) = if (operand1 >= operand2) operand1 to operand2 else operand2 to operand1
                MathChallenge(
                    difficulty = ChallengeDifficulty.EASY,
                    operand1 = a,
                    operand2 = b,
                    operator = operator,
                    answer = a - b
                )
            }
            else -> throw IllegalStateException("Unexpected operator")
        }
    }

    private fun generateMediumMathChallenge(): MathChallenge {
        // Medium: Two-digit operations or simple multiplication
        val operatorIndex = Random.nextInt(3)
        val operator = when (operatorIndex) {
            0 -> MathOperator.ADD
            1 -> MathOperator.SUBTRACT
            else -> MathOperator.MULTIPLY
        }

        return when (operator) {
            MathOperator.ADD -> {
                val operand1 = Random.nextInt(10, 50)
                val operand2 = Random.nextInt(10, 50)
                MathChallenge(
                    difficulty = ChallengeDifficulty.MEDIUM,
                    operand1 = operand1,
                    operand2 = operand2,
                    operator = operator,
                    answer = operand1 + operand2
                )
            }
            MathOperator.SUBTRACT -> {
                val operand1 = Random.nextInt(20, 100)
                val operand2 = Random.nextInt(10, operand1)
                MathChallenge(
                    difficulty = ChallengeDifficulty.MEDIUM,
                    operand1 = operand1,
                    operand2 = operand2,
                    operator = operator,
                    answer = operand1 - operand2
                )
            }
            MathOperator.MULTIPLY -> {
                val operand1 = Random.nextInt(2, 10)
                val operand2 = Random.nextInt(2, 10)
                MathChallenge(
                    difficulty = ChallengeDifficulty.MEDIUM,
                    operand1 = operand1,
                    operand2 = operand2,
                    operator = operator,
                    answer = operand1 * operand2
                )
            }
            else -> throw IllegalStateException("Unexpected operator")
        }
    }

    private fun generateHardMathChallenge(): MathChallenge {
        // Hard: Two-digit multiplication, division, or complex operations
        val operatorIndex = Random.nextInt(4)
        val operator = when (operatorIndex) {
            0 -> MathOperator.ADD
            1 -> MathOperator.SUBTRACT
            2 -> MathOperator.MULTIPLY
            else -> MathOperator.DIVIDE
        }

        return when (operator) {
            MathOperator.ADD -> {
                val operand1 = Random.nextInt(50, 200)
                val operand2 = Random.nextInt(50, 200)
                MathChallenge(
                    difficulty = ChallengeDifficulty.HARD,
                    operand1 = operand1,
                    operand2 = operand2,
                    operator = operator,
                    answer = operand1 + operand2
                )
            }
            MathOperator.SUBTRACT -> {
                val operand1 = Random.nextInt(100, 500)
                val operand2 = Random.nextInt(50, operand1)
                MathChallenge(
                    difficulty = ChallengeDifficulty.HARD,
                    operand1 = operand1,
                    operand2 = operand2,
                    operator = operator,
                    answer = operand1 - operand2
                )
            }
            MathOperator.MULTIPLY -> {
                val operand1 = Random.nextInt(10, 20)
                val operand2 = Random.nextInt(10, 20)
                MathChallenge(
                    difficulty = ChallengeDifficulty.HARD,
                    operand1 = operand1,
                    operand2 = operand2,
                    operator = operator,
                    answer = operand1 * operand2
                )
            }
            MathOperator.DIVIDE -> {
                // Ensure clean division
                val operand2 = Random.nextInt(2, 12)
                val answer = Random.nextInt(5, 20)
                val operand1 = operand2 * answer
                MathChallenge(
                    difficulty = ChallengeDifficulty.HARD,
                    operand1 = operand1,
                    operand2 = operand2,
                    operator = operator,
                    answer = answer
                )
            }
        }
    }

    /**
     * Generate a memory challenge
     */
    fun generateMemoryChallenge(difficulty: ChallengeDifficulty): MemoryChallenge {
        val length = when (difficulty) {
            ChallengeDifficulty.EASY -> 4
            ChallengeDifficulty.MEDIUM -> 6
            ChallengeDifficulty.HARD -> 8
        }

        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        val sequence = (1..length).map { chars.random() }.joinToString("")

        return MemoryChallenge(difficulty, sequence)
    }

    /**
     * Generate a pattern challenge
     */
    fun generatePatternChallenge(difficulty: ChallengeDifficulty): PatternChallenge {
        return when (difficulty) {
            ChallengeDifficulty.EASY -> generateEasyPatternChallenge()
            ChallengeDifficulty.MEDIUM -> generateMediumPatternChallenge()
            ChallengeDifficulty.HARD -> generateHardPatternChallenge()
        }
    }

    private fun generateEasyPatternChallenge(): PatternChallenge {
        // Simple arithmetic sequence
        val start = Random.nextInt(1, 10)
        val step = Random.nextInt(1, 5)
        val pattern = (0..4).map { start + it * step }
        val missingIndex = Random.nextInt(1, 4)

        return PatternChallenge(
            difficulty = ChallengeDifficulty.EASY,
            pattern = pattern,
            missingIndex = missingIndex,
            answer = pattern[missingIndex]
        )
    }

    private fun generateMediumPatternChallenge(): PatternChallenge {
        // Could be arithmetic or simple multiplication
        val isMultiply = Random.nextBoolean()
        val pattern = if (isMultiply) {
            val base = Random.nextInt(2, 5)
            (0..4).map { base * (it + 1) }
        } else {
            val start = Random.nextInt(5, 20)
            val step = Random.nextInt(3, 8)
            (0..4).map { start + it * step }
        }
        val missingIndex = Random.nextInt(1, 4)

        return PatternChallenge(
            difficulty = ChallengeDifficulty.MEDIUM,
            pattern = pattern,
            missingIndex = missingIndex,
            answer = pattern[missingIndex]
        )
    }

    private fun generateHardPatternChallenge(): PatternChallenge {
        // Fibonacci-like or geometric sequence
        val isFibonacci = Random.nextBoolean()
        val pattern = if (isFibonacci) {
            val a = Random.nextInt(1, 5)
            val b = Random.nextInt(1, 5)
            generateSequence(a to b) { (prev1, prev2) -> prev2 to (prev1 + prev2) }
                .take(5)
                .map { it.first }
                .toList()
        } else {
            // Squares sequence
            val start = Random.nextInt(1, 5)
            (0..4).map { (start + it) * (start + it) }
        }
        val missingIndex = Random.nextInt(2, 4)

        return PatternChallenge(
            difficulty = ChallengeDifficulty.HARD,
            pattern = pattern,
            missingIndex = missingIndex,
            answer = pattern[missingIndex]
        )
    }
}
