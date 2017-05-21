package io.spidey

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SpideyApplication

fun main(args: Array<String>) {
    SpringApplication.run(SpideyApplication::class.java, *args)
}
