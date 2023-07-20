Purpose:
This app is a back-end only clone of Wordle, with a few changes.
Its purpose is to provide a demo of a simple Kotlin Spring Boot application, with testing and endpoints.
In the future there will, hopefully, be fordle in other languages as a contrast to Kotlin.
It is NOT meant to function like a full-fledged game of Wordle, although it may be modified to that point eventually.
It is also not truly RESTful.

Endpoints:
GET /words/validate/{word} - Checks that word is the correct length (5) and in our dictionary
Ex: /words/validate/word returns false, as it's only 4 letters
/words/validate/asdfg returns false, as it's not in our dictionary
/words/validate/salve returns true

GET /words/start - Generates and return a random word from our dictionary for use as the target in a game

GET /words/guess?target={target}&guess={guess} - Guesses a word and returns the typical Wordle colored response
Ex: localhost:8080/words/guess?target=mucky&guess=mucky returns all green, and the won flag
/words/guess?target=mucky&guess=young returns two yellow and three gray, and the won flag is false
/words/guess?target=mucky&guess=asdfg throws a 400 error, since asdfg is not in our dictionary

To run this you need a Java runtime. Then navigate to this package and enter './gradlew bootRun' into the terminal.