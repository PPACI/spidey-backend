package io.spidey.Controller

import io.reactivex.Flowable
import io.reactivex.Single
import io.spidey.Models.SigmaJsGraph
import io.spidey.Models.TwitterUser
import io.spidey.Services.GraphService
import io.spidey.Services.ProfileService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.social.twitter.api.Tweet
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class MainController constructor(val twitterService: GraphService, val profileService: ProfileService) {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass.name)

    @GetMapping("/user/{screenName}")
    fun getUser(@PathVariable screenName: String): Single<TwitterUser> {
        logger.info("[getUser] for user: $screenName")

        return this.profileService.getUserDetails(screenName)
    }

    @GetMapping("/graph/{screenName}")
    fun getUserGraph(@PathVariable screenName: String): Single<SigmaJsGraph> {
        logger.info("[getUserGraph] for user: $screenName")

        return this.twitterService.buildGraph(screenName)
    }

}