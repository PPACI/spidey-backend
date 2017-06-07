package io.spidey.Controller

import io.reactivex.Flowable
import io.reactivex.Single
import io.spidey.Services.TwitterService
import org.springframework.social.twitter.api.Tweet
import org.springframework.social.twitter.api.TwitterProfile
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class HelloWorldController constructor(val TwitterService: TwitterService) {


    @GetMapping
    fun hello(): Single<String> = Single.just("hello world").map { it.toUpperCase() }

    @GetMapping("/search")
    fun HelloSearch(@RequestParam("number") number:Int?): Flowable<Tweet> {
        return this.TwitterService.getHelloWorldTweets(number = number ?: 1)
    }

    @GetMapping("/user/{user_id}")
    fun getUserTimeline(@PathVariable user_id:String): Flowable<TwitterProfile>{
        return this.TwitterService.getUserTimeline(user_id)
    }

    @GetMapping("/graph/{user_id}")
    fun getUserGraph(@PathVariable user_id:String): Flowable<TwitterProfile>{
        return this.TwitterService.getUserGraph(user_id)
    }

}