package com.dialexa.fordle.service

import com.dialexa.fordle.entity.ColorEnum
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*

internal class WordsServiceTest {
    private lateinit var wordsService: WordsService

    @BeforeEach
    fun setUp() {
        wordsService = WordsService()
    }

    @Test
    fun isWordValid_too_long() {
        val actual = wordsService.isWordValid("this is too long")
        assertFalse(actual)
    }

    @Test
    fun isWordValid_not_a_word() {
        val actual = wordsService.isWordValid("anach")
        assertFalse(actual)
    }

    @Test
    fun isWordValid_true() {
        val actual = wordsService.isWordValid("salve")
        assertTrue(actual)
    }

    @Test
    fun getTargetWord_returns_word() {
        val actual = wordsService.getTargetWord()
        assertNotEquals("", actual)
    }

    @Test
    fun makeGuess_unequal_length() {
        val b64Target = Base64.getEncoder().encodeToString("targe".toByteArray())
        val ex = assertThrows(ResponseStatusException::class.java) { wordsService.makeGuess(b64Target, "target") }
        assertEquals(HttpStatus.BAD_REQUEST, ex.statusCode)
    }

    @Test
    fun makeGuess_mixed_guess() {
        val b64Target = Base64.getEncoder().encodeToString("salve".toByteArray())
        val actual = wordsService.makeGuess(b64Target, "large")
        assertAll(
            { assertFalse(actual.won) },
            { assertEquals(5, actual.letters.size) },
            { assertEquals(ColorEnum.YELLOW, actual.letters[0].color) },
            { assertEquals('l', actual.letters[0].letter) },
            { assertEquals(ColorEnum.GREEN, actual.letters[1].color) },
            { assertEquals('a', actual.letters[1].letter) },
            { assertEquals(ColorEnum.GRAY, actual.letters[2].color) },
            { assertEquals('r', actual.letters[2].letter) },
            { assertEquals(ColorEnum.GRAY, actual.letters[3].color) },
            { assertEquals('g', actual.letters[3].letter) },
            { assertEquals(ColorEnum.GREEN, actual.letters[4].color) },
            { assertEquals('e', actual.letters[4].letter) }
        )
    }

    @Test
    fun makeGuess_handles_multiples() {
        val b64Target = Base64.getEncoder().encodeToString("loose".toByteArray())
        val actual = wordsService.makeGuess(b64Target, "folly")
        assertAll(
            { assertFalse(actual.won) },
            { assertEquals(5, actual.letters.size) },
            { assertEquals(ColorEnum.GRAY, actual.letters[0].color) },
            { assertEquals('f', actual.letters[0].letter) },
            { assertEquals(ColorEnum.GREEN, actual.letters[1].color) },
            { assertEquals('o', actual.letters[1].letter) },
            { assertEquals(ColorEnum.YELLOW, actual.letters[2].color) },
            { assertEquals('l', actual.letters[2].letter) },
            { assertEquals(ColorEnum.GRAY, actual.letters[3].color) },
            { assertEquals('l', actual.letters[3].letter) },
            { assertEquals(ColorEnum.GRAY, actual.letters[4].color) },
            { assertEquals('y', actual.letters[4].letter) }
        )
    }

    @Test
    fun makeGuess_anagram_guess() {
        val b64Target = Base64.getEncoder().encodeToString("smile".toByteArray())
        val actual = wordsService.makeGuess(b64Target, "slime")
        assertAll(
            { assertTrue(actual.won) },
            { assertEquals(5, actual.letters.size) },
            { assertEquals(ColorEnum.GREEN, actual.letters[0].color) },
            { assertEquals(ColorEnum.YELLOW, actual.letters[1].color) },
            { assertEquals(ColorEnum.GREEN, actual.letters[2].color) },
            { assertEquals(ColorEnum.YELLOW, actual.letters[3].color) },
            { assertEquals(ColorEnum.GREEN, actual.letters[4].color) }
        )
    }

    @Test
    fun makeGuess_correct_guess() {
        val b64Target = Base64.getEncoder().encodeToString("salve".toByteArray())
        val actual = wordsService.makeGuess(b64Target, "salve")
        assertTrue(actual.won)
        assertEquals(5, actual.letters.size)
        for (letter in actual.letters) {
            assertEquals(ColorEnum.GREEN, letter.color)
        }
    }
}