package io.spidey.Controller

import io.reactivex.Flowable
import io.reactivex.Single
import io.spidey.Services.TwitterService
import org.springframework.social.twitter.api.Tweet
import org.springframework.social.twitter.api.TwitterProfile
import org.springframework.social.twitter.api.impl.TwitterTemplate
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/")
class HelloWorldController constructor(val TwitterService: TwitterService) {


    @GetMapping
    fun hello(): Single<String> = Single.just("hello world").map { it.toUpperCase() }

    @GetMapping("/search")
    fun hello_search(@RequestParam("number") number:Int?): Flowable<Tweet> {
        return this.TwitterService.getHelloWorldTweets(number = number ?: 1)
    }

    @GetMapping("/user/{user_id}")
    fun get_user(@PathVariable user_id:String): Flowable<TwitterProfile>{
        return this.TwitterService.getUser(user_id)
    }

}