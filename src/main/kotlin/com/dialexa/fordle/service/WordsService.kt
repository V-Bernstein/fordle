package com.dialexa.fordle.service

import com.dialexa.fordle.entity.ColorEnum
import com.dialexa.fordle.entity.GameResponse
import com.dialexa.fordle.entity.LetterCode
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import org.springframework.web.server.ResponseStatusException

@Service
class WordsService {
    private final val WORD_LENGTH = 5 // TODO: User selects word length

    fun isWordValid(word: String): Boolean {
        if (word.length != WORD_LENGTH) {
            return false
        }

        // read in list of acceptable words
        val words = getWordList()
        // find word (boolean search)
        return isWordInWordList(words, word)
    }

    fun getTargetWord(): String {
        val words = getWordList()
        return words.random()
    }

    fun makeGuess(target: String, guess: String): GameResponse {
        if (guess.length != target.length || !isWordValid(guess)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }

        // TODO: Track attempts as state; reject when max attempts reached

        val tgtLetterMap: MutableMap<Char, MutableList<Int>> = mutableMapOf()
        for (i in target.indices) { // Fill the target letter map
            val letter = target[i]
            if (tgtLetterMap.containsKey(letter)) {
                val indices = tgtLetterMap[letter]!!
                indices.add(i) // Add index to map
            } else {
                val tempList: MutableList<Int> = mutableListOf(i)
                tgtLetterMap[letter] = tempList
            }
        }

        val letterList: MutableList<LetterCode> = mutableListOf()
        for (i in guess.indices) {
            val letter = guess[i]
            if (tgtLetterMap.containsKey(letter)) {
                val indices = tgtLetterMap[letter]!!
                if (i in indices) { // Correct letter in correct spot
                    letterList.add(LetterCode(letter, ColorEnum.GREEN))
                    indices.remove(i) // Remove it to handle case of multiple of that letter
                    continue
                }
            }
            letterList.add(LetterCode(letter, ColorEnum.GRAY))
        }

        // Second loop handles case of multiples where GREEN was removed
        for (i in guess.indices) {
            val letter = guess[i]
            if (tgtLetterMap.containsKey(letter) && letterList[i].color == ColorEnum.GRAY) {
                val indices = tgtLetterMap[letter]!!
                if (indices.isNotEmpty()) { // If indices is empty we've "used" this letter
                    letterList[i] = LetterCode(letter, ColorEnum.YELLOW)
                    indices.removeFirst() // Doesn't matter what index you remove, so just remove one
                }
            }
        }

        // Win conditions:
        // All letters are green OR
        // The guess is an anagram of the target
        // (special condition based on a peeve of mine, like guessing smile when it's slime)
        // Note: This can boil down to whether the guess is an anagram of the target or not
        return GameResponse(letterList, isAnagram(target, guess))
    }

    // TODO: Pull these out into a helper class for better testing
    private fun getWordList(): List<String> {
        val file = ResourceUtils.getFile("classpath:static/words")
        return file.useLines { lines -> lines.toList() }
    }

    // words is sorted, so we can use a boolean search
    private fun isWordInWordList(words: List<String>, word: String): Boolean {
        var low = 0
        var high = words.lastIndex
        var mid = high / 2

        while (low <= high) {
            val midWord = words[mid]
            if (word == midWord) {
                return true
            } else if (word < midWord) {
                high = mid - 1
                mid = (low + high) / 2
            } else { // word > midWord
                low = mid + 1
                mid = (low + high) / 2
            }
        }
        return false
    }

    private fun isAnagram(a: String, b: String): Boolean {
        if (a.length != b.length) {
            return false
        }

        val letterMap: MutableMap<Char, Int> = mutableMapOf()
        for (letter in a) {
            addToMap(letterMap, letter)
        }
        for (letter in b) {
            if (letterMap.containsKey(letter)) {
                val curCount = letterMap[letter]!!
                if (curCount == 0) {
                    return false // not an anagram
                }
                letterMap[letter] = curCount - 1
            } else {
                return false // not an anagram
            }
        }
        return true
    }

    private fun addToMap(map: MutableMap<Char, Int>, letter: Char) {
        if (map.containsKey(letter)) {
            val curCount = map[letter]!!
            map[letter] = curCount + 1
        } else {
            map[letter] = 1
        }
    }
}