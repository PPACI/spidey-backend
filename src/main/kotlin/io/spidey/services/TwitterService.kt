package io.spidey.services

import io.reactivex.Single
import org.springframework.social.NotAuthorizedException
import org.springframework.social.ResourceNotFoundException
import org.springframework.social.twitter.api.Tweet
import org.springframework.social.twitter.api.TwitterProfile
import org.springframework.social.twitter.api.impl.TwitterTemplate
import org.springframework.stereotype.Service

@Service
class TwitterService {

    val twitter = TwitterTemplate("qPe0o5axovP4VlGnv9bDchVwI", "yFWHo5mHwIy99jlTfgp2loSpnRlGLMwRvpNJqqFC6NQQfqal15")

    fun getTwitterProfileForScreenName(screenName: String): Single<TwitterProfile> {
        return Single.just(this.twitter.userOperations().getUserProfile(screenName))
    }

    fun getUserTimelineForScreenName(screenName: String): List<Tweet> {
        try {
            return this.twitter.timelineOperations().getUserTimeline(screenName, 200)
        } catch (e: NotAuthorizedException) {
            return emptyList()
        } catch (e: ResourceNotFoundException) {
            return emptyList()
        }

    }
}