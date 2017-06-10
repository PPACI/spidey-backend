package io.spidey.Controller

import io.reactivex.Flowable
import io.reactivex.Single
import io.spidey.Models.SigmaJsGraph
import io.spidey.Services.TwitterService
import org.slf4j.LoggerFactory
import org.springframework.social.twitter.api.Tweet
import org.springframework.social.twitter.api.TwitterProfile
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class HelloWorldController constructor(val TwitterService: TwitterService) {
    val logger = LoggerFactory.getLogger(this.javaClass.name)

    @GetMapping
    fun hello(): Single<String> = Single.just("hello world").map { it.toUpperCase() }

    @GetMapping("/search")
    fun HelloSearch(@RequestParam("number") number: Int?): Flowable<Tweet> {
        return this.TwitterService.getHelloWorldTweets(number = number ?: 1)
    }

    @GetMapping("/user/{user_id}")
    fun getUserTimeline(@PathVariable user_id: String): Flowable<TwitterProfile> {
        return this.TwitterService.getUserTimeline(user_id)
    }

    @GetMapping("/graph/{screen_name}")
    fun getUserGraph(@PathVariable screen_name: String): SigmaJsGraph {
        val graph =  this.TwitterService.getUserGraph(screen_name)
        this.logger.info("Graph report for ${screen_name}: ${graph.nodes.size} nodes and ${graph.edges.size} edges")
        return graph
    }

}