package io.spidey.Controller

import io.reactivex.Flowable
import io.reactivex.Single
import org.springframework.social.twitter.api.Tweet
import org.springframework.social.twitter.api.impl.TwitterTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class HelloWorldController constructor() {
    val twitter = TwitterTemplate("qPe0o5axovP4VlGnv9bDchVwI", "yFWHo5mHwIy99jlTfgp2loSpnRlGLMwRvpNJqqFC6NQQfqal15")

    @GetMapping
    fun hello(): Single<String> = Single.just("hello world").map { it.toUpperCase() }

    @GetMapping("/twitter")
    fun hello_search(): Flowable<Tweet> {
        return Flowable.fromIterable(this.twitter.searchOperations().search("hello world").tweets)
    }

}