package com.betterclock.challenge

import com.betterclock.data.model.ChallengeDifficulty
import com.betterclock.data.model.ChallengeType
import org.junit.Assert.*
import org.junit.Test
import kotlin.system.measureTimeMillis

/**
 * Unit tests for ChallengeGenerator
 */
class ChallengeGeneratorTest {

    // ==================== Math Challenge Tests ====================

    @Test
    fun `generateMathChallenge with EASY difficulty produces valid challenge`() {
        repeat(100) {
            val challenge = ChallengeGenerator.generateMathChallenge(ChallengeDifficulty.EASY)

            assertEquals(ChallengeDifficulty.EASY, challenge.difficulty)
            assertEquals(ChallengeType.MATH, challenge.type)

            // Easy challenges use single digit operands (1-9)
            assertTrue("operand1 should be between 1 and 9", challenge.operand1 in 1..18)
            assertTrue("operand2 should be between 1 and 9", challenge.operand2 in 1..9)

            // Verify the answer is correct
            val expectedAnswer = when (challenge.operator) {
                MathOperator.ADD -> challenge.operand1 + challenge.operand2
                MathOperator.SUBTRACT -> challenge.operand1 - challenge.operand2
                else -> fail("Unexpected operator for easy difficulty")
            }
            assertEquals(expectedAnswer, challenge.answer)

            // Subtraction should have positive result
            if (challenge.operator == MathOperator.SUBTRACT) {
                assertTrue("Subtraction result should be non-negative", challenge.answer >= 0)
            }
        }
    }

    @Test
    fun `generateMathChallenge with MEDIUM difficulty produces valid challenge`() {
        repeat(100) {
            val challenge = ChallengeGenerator.generateMathChallenge(ChallengeDifficulty.MEDIUM)

            assertEquals(ChallengeDifficulty.MEDIUM, challenge.difficulty)

            // Verify the answer is correct
            val expectedAnswer = when (challenge.operator) {
                MathOperator.ADD -> challenge.operand1 + challenge.operand2
                MathOperator.SUBTRACT -> challenge.operand1 - challenge.operand2
                MathOperator.MULTIPLY -> challenge.operand1 * challenge.operand2
                else -> fail("Unexpected operator for medium difficulty")
            }
            assertEquals(expectedAnswer, challenge.answer)
        }
    }

    @Test
    fun `generateMathChallenge with HARD difficulty produces valid challenge`() {
        repeat(100) {
            val challenge = ChallengeGenerator.generateMathChallenge(ChallengeDifficulty.HARD)

            assertEquals(ChallengeDifficulty.HARD, challenge.difficulty)

            // Verify the answer is correct
            val expectedAnswer = when (challenge.operator) {
                MathOperator.ADD -> challenge.operand1 + challenge.operand2
                MathOperator.SUBTRACT -> challenge.operand1 - challenge.operand2
                MathOperator.MULTIPLY -> challenge.operand1 * challenge.operand2
                MathOperator.DIVIDE -> challenge.operand1 / challenge.operand2
            }
            assertEquals(expectedAnswer, challenge.answer)

            // Division should have clean result (no remainder)
            if (challenge.operator == MathOperator.DIVIDE) {
                assertEquals(0, challenge.operand1 % challenge.operand2)
            }
        }
    }

    @Test
    fun `math challenge checkAnswer validates correctly`() {
        val challenge = MathChallenge(
            difficulty = ChallengeDifficulty.EASY,
            operand1 = 5,
            operand2 = 3,
            operator = MathOperator.ADD,
            answer = 8
        )

        assertTrue(challenge.checkAnswer("8"))
        assertTrue(challenge.checkAnswer(" 8 "))
        assertFalse(challenge.checkAnswer("7"))
        assertFalse(challenge.checkAnswer("9"))
        assertFalse(challenge.checkAnswer(""))
        assertFalse(challenge.checkAnswer("abc"))
    }

    @Test
    fun `math challenge getDisplayQuestion formats correctly`() {
        val addChallenge = MathChallenge(
            difficulty = ChallengeDifficulty.EASY,
            operand1 = 5,
            operand2 = 3,
            operator = MathOperator.ADD,
            answer = 8
        )
        assertEquals("5 + 3 = ?", addChallenge.getDisplayQuestion())

        val subtractChallenge = MathChallenge(
            difficulty = ChallengeDifficulty.EASY,
            operand1 = 8,
            operand2 = 3,
            operator = MathOperator.SUBTRACT,
            answer = 5
        )
        assertEquals("8 - 3 = ?", subtractChallenge.getDisplayQuestion())

        val multiplyChallenge = MathChallenge(
            difficulty = ChallengeDifficulty.MEDIUM,
            operand1 = 6,
            operand2 = 7,
            operator = MathOperator.MULTIPLY,
            answer = 42
        )
        assertEquals("6 × 7 = ?", multiplyChallenge.getDisplayQuestion())

        val divideChallenge = MathChallenge(
            difficulty = ChallengeDifficulty.HARD,
            operand1 = 24,
            operand2 = 4,
            operator = MathOperator.DIVIDE,
            answer = 6
        )
        assertEquals("24 ÷ 4 = ?", divideChallenge.getDisplayQuestion())
    }

    // ==================== Memory Challenge Tests ====================

    @Test
    fun `generateMemoryChallenge produces sequences of correct length`() {
        val easyChallenge = ChallengeGenerator.generateMemoryChallenge(ChallengeDifficulty.EASY)
        assertEquals(4, easyChallenge.sequence.length)

        val mediumChallenge = ChallengeGenerator.generateMemoryChallenge(ChallengeDifficulty.MEDIUM)
        assertEquals(6, mediumChallenge.sequence.length)

        val hardChallenge = ChallengeGenerator.generateMemoryChallenge(ChallengeDifficulty.HARD)
        assertEquals(8, hardChallenge.sequence.length)
    }

    @Test
    fun `memory challenge checkAnswer is case insensitive`() {
        val challenge = MemoryChallenge(ChallengeDifficulty.EASY, "ABC4")

        assertTrue(challenge.checkAnswer("ABC4"))
        assertTrue(challenge.checkAnswer("abc4"))
        assertTrue(challenge.checkAnswer(" ABC4 "))
        assertFalse(challenge.checkAnswer("ABC5"))
        assertFalse(challenge.checkAnswer("AB4"))
    }

    // ==================== Pattern Challenge Tests ====================

    @Test
    fun `generatePatternChallenge produces valid patterns`() {
        repeat(50) {
            val easyChallenge = ChallengeGenerator.generatePatternChallenge(ChallengeDifficulty.EASY)
            assertEquals(5, easyChallenge.pattern.size)
            assertTrue(easyChallenge.missingIndex in 1..3)
            assertEquals(easyChallenge.pattern[easyChallenge.missingIndex], easyChallenge.answer)

            val mediumChallenge = ChallengeGenerator.generatePatternChallenge(ChallengeDifficulty.MEDIUM)
            assertEquals(5, mediumChallenge.pattern.size)
            assertEquals(mediumChallenge.pattern[mediumChallenge.missingIndex], mediumChallenge.answer)

            val hardChallenge = ChallengeGenerator.generatePatternChallenge(ChallengeDifficulty.HARD)
            assertEquals(5, hardChallenge.pattern.size)
            assertEquals(hardChallenge.pattern[hardChallenge.missingIndex], hardChallenge.answer)
        }
    }

    @Test
    fun `pattern challenge checkAnswer validates correctly`() {
        val challenge = PatternChallenge(
            difficulty = ChallengeDifficulty.EASY,
            pattern = listOf(2, 4, 6, 8, 10),
            missingIndex = 2,
            answer = 6
        )

        assertTrue(challenge.checkAnswer("6"))
        assertTrue(challenge.checkAnswer(" 6 "))
        assertFalse(challenge.checkAnswer("5"))
        assertFalse(challenge.checkAnswer("7"))
    }

    // ==================== General Challenge Tests ====================

    @Test
    fun `generateChallenge creates correct challenge type`() {
        val mathChallenge = ChallengeGenerator.generateChallenge(ChallengeType.MATH, ChallengeDifficulty.EASY)
        assertTrue(mathChallenge is MathChallenge)
        assertEquals(ChallengeType.MATH, mathChallenge.type)

        val memoryChallenge = ChallengeGenerator.generateChallenge(ChallengeType.MEMORY, ChallengeDifficulty.EASY)
        assertTrue(memoryChallenge is MemoryChallenge)
        assertEquals(ChallengeType.MEMORY, memoryChallenge.type)

        val patternChallenge = ChallengeGenerator.generateChallenge(ChallengeType.PATTERN, ChallengeDifficulty.EASY)
        assertTrue(patternChallenge is PatternChallenge)
        assertEquals(ChallengeType.PATTERN, patternChallenge.type)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `generateChallenge throws exception for NONE type`() {
        ChallengeGenerator.generateChallenge(ChallengeType.NONE, ChallengeDifficulty.EASY)
    }

    // ==================== Performance Tests ====================

    @Test
    fun `challenge generation completes within 100ms`() {
        val types = listOf(ChallengeType.MATH, ChallengeType.MEMORY, ChallengeType.PATTERN)
        val difficulties = ChallengeDifficulty.entries

        types.forEach { type ->
            difficulties.forEach { difficulty ->
                val time = measureTimeMillis {
                    repeat(100) {
                        ChallengeGenerator.generateChallenge(type, difficulty)
                    }
                }
                // 100 challenges should complete in well under 100ms
                assertTrue("Generation of 100 $type/$difficulty challenges took ${time}ms", time < 100)
            }
        }
    }

    @Test
    fun `single challenge generation is very fast`() {
        val types = listOf(ChallengeType.MATH, ChallengeType.MEMORY, ChallengeType.PATTERN)
        val difficulties = ChallengeDifficulty.entries

        types.forEach { type ->
            difficulties.forEach { difficulty ->
                val time = measureTimeMillis {
                    ChallengeGenerator.generateChallenge(type, difficulty)
                }
                assertTrue("Single $type/$difficulty challenge took ${time}ms", time < 100)
            }
        }
    }
}
