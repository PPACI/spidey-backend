package io.spidey.Controller

import io.spidey.Models.SigmaJsGraph
import io.spidey.Services.TwitterService
import org.slf4j.LoggerFactory
import org.springframework.social.twitter.api.TwitterProfile
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class HelloWorldController constructor(val TwitterService: TwitterService) {
    val logger = LoggerFactory.getLogger(this.javaClass.name)

    @GetMapping("/user/{screen_name}")
    fun getUserTimeline(@PathVariable screen_name: String): TwitterProfile {
        return this.TwitterService.getUser(screen_name)
    }

    @GetMapping("/graph/{screen_name}")
    fun getUserGraph(@PathVariable screen_name: String): SigmaJsGraph {
        val graph = this.TwitterService.getUserGraph(screen_name)
        this.logger.info("Graph report for $screen_name: ${graph.nodes.size} nodes and ${graph.edges.size} edges")
        return graph
    }

}