package io.spidey.Controller

import io.reactivex.Flowable
import io.reactivex.Single
import io.spidey.Services.TwitterService
import org.springframework.social.twitter.api.Tweet
import org.springframework.social.twitter.api.impl.TwitterTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/")
class HelloWorldController constructor(val TwitterService: TwitterService) {


    @GetMapping
    fun hello(): Single<String> = Single.just("hello world").map { it.toUpperCase() }

    @GetMapping("/twitter")
    fun hello_search(@RequestParam("number", required = false) number:Int?): Flowable<Tweet> {
        return this.TwitterService.getHelloWorldTweets(number = number ?: 1)
    }

}