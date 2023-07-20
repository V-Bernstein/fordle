package com.dialexa.fordle.controller

import com.dialexa.fordle.entity.GameResponse
import com.dialexa.fordle.service.WordsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class WordsController(val wordsService: WordsService) {

    @GetMapping("/words/validate/{word}")
    fun isWordValid(@PathVariable(name = "word") word: String): Boolean {
        return wordsService.isWordValid(word)
    }

    @GetMapping("/words/start")
    fun getTargetWord(): String {
        // TODO: This should be state, or an instanceId tied to a DB
        return wordsService.getTargetWord()
    }

    @GetMapping("/words/guess")
    fun makeGuess(@RequestParam(name = "target") target: String,
                  @RequestParam(name = "guess") guess: String): GameResponse {
        return wordsService.makeGuess(target, guess)
    }
}