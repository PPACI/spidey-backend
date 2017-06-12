package io.spidey.Controller

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.spidey.Models.SigmaJsGraph
import io.spidey.Models.TwitterUser
import io.spidey.Services.TwitterService
import org.slf4j.LoggerFactory
import org.springframework.social.twitter.api.Tweet
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class HelloWorldController constructor(val twitterService: TwitterService) {
    val logger = LoggerFactory.getLogger(this.javaClass.name)

    @GetMapping
    fun hello(): Single<String> = Single.just("hello world").map { it.toUpperCase() }

    @GetMapping("/search")
    fun HelloSearch(@RequestParam("number") number: Int?): Flowable<Tweet> {
        return this.twitterService.getHelloWorldTweets(number = number ?: 1)
    }

    @GetMapping("/user/{screenName}")
    fun getUser(@PathVariable screenName: String): Single<TwitterUser> {
        logger.info("[getUserTimer] for user: $screenName")

        return this.twitterService.getUserDetails(screenName)
    }

    @GetMapping("/graph/{screenName}")
    fun getUserGraph(@PathVariable screenName: String): SigmaJsGraph {
        logger.info("[buildGraph] for user: $screenName")

        val graph =  this.twitterService.buildGraph(screenName)
        logger.info("[buildGraph] Graph results: ${graph.nodes.size} nodes and ${graph.edges.size} edges")

        return graph
    }

}