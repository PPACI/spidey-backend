package io.spidey.Services

import io.reactivex.Flowable
import org.springframework.social.twitter.api.SearchParameters
import org.springframework.social.twitter.api.Tweet
import org.springframework.social.twitter.api.TwitterProfile
import org.springframework.social.twitter.api.impl.TwitterTemplate
import org.springframework.stereotype.Service

@Service
class TwitterService {
    val twitter = TwitterTemplate("qPe0o5axovP4VlGnv9bDchVwI", "yFWHo5mHwIy99jlTfgp2loSpnRlGLMwRvpNJqqFC6NQQfqal15")

    fun getHelloWorldTweets(number: Int): Flowable<Tweet> {
        return Flowable.fromIterable(this.twitter.searchOperations().search(
                SearchParameters("hello world").count(number)
        ).tweets)
    }

    fun getUserTimeline(user_id: String): Flowable<TwitterProfile> {
        return Flowable.just(this.twitter.userOperations().getUserProfile(user_id))
    }

    fun getUserGraph(user_id: String): Flowable<TwitterProfile> {
        return Flowable.fromIterable(this.twitter.timelineOperations().getFavorites(user_id))
                .map { it.fromUserId }
                .distinct()
                .flatMap { Flowable.fromIterable(this.twitter.timelineOperations().getFavorites(it)) }
                .map { it.fromUserId }
                .distinct()
                .flatMap { Flowable.fromIterable(this.twitter.timelineOperations().getFavorites(it)) }
                .distinct()
                .map { it.user }
    }
}