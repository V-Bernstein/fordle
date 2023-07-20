package com.dialexa.fordle.controller

import com.dialexa.fordle.entity.ColorEnum
import com.dialexa.fordle.entity.GameResponse
import com.dialexa.fordle.entity.LetterCode
import com.dialexa.fordle.service.WordsService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

internal class WordsControllerTest {
    private lateinit var wordsService: WordsService
    private lateinit var wordsController: WordsController

    @BeforeEach
    fun setUp() {
        wordsService = Mockito.mock(WordsService::class.java)
        wordsController = WordsController(wordsService)
    }

    @Test
    fun isWordValid_too_long() {
        val word = "this is too long"
        Mockito.`when`(wordsService. isWordValid(word)).thenReturn(false)
        val actual = wordsController.isWordValid(word)
        assertFalse(actual)
        Mockito.verify(wordsService).isWordValid(word)
    }

    @Test
    fun isWordValid_not_a_word() {
        val word = "anach"
        Mockito.`when`(wordsService. isWordValid(word)).thenReturn(false)
        val actual = wordsController.isWordValid(word)
        assertFalse(actual)
        Mockito.verify(wordsService).isWordValid(word)
    }

    @Test
    fun isWordValid_true() {
        val word = "salve"
        Mockito.`when`(wordsService. isWordValid(word)).thenReturn(true)
        val actual = wordsController.isWordValid(word)
        assertTrue(actual)
        Mockito.verify(wordsService).isWordValid(word)
    }

    @Test
    fun getTargetWord_calls_service() {
        val expected = "salve"
        Mockito.`when`(wordsService. getTargetWord()).thenReturn(expected)
        val actual = wordsController.getTargetWord()
        assertEquals(expected, actual)
        Mockito.verify(wordsService).getTargetWord()
    }

    @Test
    fun makeGuess_calls_service() {
        val letterList = listOf(LetterCode('A', ColorEnum.GRAY),
            LetterCode('B', ColorEnum.YELLOW))
        val expected = GameResponse(letterList, false)
        val target = "goals"
        val guess = "salve"
        Mockito.`when`(wordsService. makeGuess(target, guess)).thenReturn(expected)
        val actual = wordsController.makeGuess(target, guess)
        assertEquals(expected, actual)
        Mockito.verify(wordsService).makeGuess(target, guess)
    }
}