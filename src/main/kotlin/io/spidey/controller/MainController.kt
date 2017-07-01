package io.spidey.controller

import io.reactivex.Single
import io.spidey.models.SigmaJsGraph
import io.spidey.models.TwitterUser
import io.spidey.services.GraphService
import io.spidey.services.ProfileService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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