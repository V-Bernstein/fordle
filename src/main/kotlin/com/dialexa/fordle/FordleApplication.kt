package com.dialexa.fordle

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FordleApplication

fun main(args: Array<String>) {
	runApplication<FordleApplication>(*args)
}
