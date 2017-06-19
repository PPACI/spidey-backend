package io.spidey.Services

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.spidey.Models.Node
import io.spidey.Models.SigmaJsGraph
import io.spidey.Models.TwitterUser
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

    fun getUserDetails(screenName: String): Single<TwitterUser> {
        return Single.just(this.twitter.userOperations().getUserProfile(screenName))
                .map { profileToUser(it) }
    }

    /**
     * TODO: use the user_timeline (https://dev.twitter.com/rest/reference/get/statuses/user_timeline)
     * It returns an array of tweets from the selected screenName including retweets and replies.
     * In case of retweet, we could extract the original tweet then the author.
     * In case of reply, we could directly extract the author.
     * A function to do this job depending on retweet/reply status and returning a user_name should be done.
     */
    fun buildGraph(screenName: String): Single<SigmaJsGraph> {

        val firstLevel = Observable
                .fromIterable(getPairsOfRelation(screenName))
                .take(200)

        val secondLevel = firstLevel
                .flatMap { pair -> Observable.fromIterable(getPairsOfRelation(pair.second)) }
                .take(400)

        val thirdLevel = secondLevel
                .flatMap { pair -> Observable.fromIterable(getPairsOfRelation(pair.second)) }
                .take(600)

        return Observable.merge(firstLevel, secondLevel, thirdLevel)
                .distinct()
                .map { Pair(Node(it.first), Node(it.second)) }
                .reduceWith({ SigmaJsGraph() }, { graph, pair -> graph.addRelation(sourceNode = pair.first, targetNode = pair.second) })
                .map { it.trimMonoEdgeUser() }

    }

    private fun getPairsOfRelation(screen_name: String): List<Pair<String, String>> {
        return this.twitter.timelineOperations().getUserTimeline(screen_name, 200)
                .map { it.retweetedStatus?.fromUser ?: it.inReplyToScreenName }
                .distinct()
                .map { Pair(screen_name, it) }
    }

    private fun profileToUser(profile: TwitterProfile): TwitterUser {
        return TwitterUser(
                userName = profile.screenName,
                description = profile.description,
                profilePictureUrl = profile.profileImageUrl,
                bannerPictureUrl = profile.profileBannerUrl
        )
    }
}