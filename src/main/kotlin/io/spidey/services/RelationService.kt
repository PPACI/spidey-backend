package io.spidey.services

import io.reactivex.Observable
import io.spidey.models.Tweet
import io.spidey.repository.TweetRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*


@Service
class RelationService(val twitterService: TwitterService, val tweetRepository: TweetRepository, val tweetSaverService: TweetSaverService) {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass.name)

    fun getPairsOfRelation(screenName: String, limit: Int): Observable<Pair<String, String>> {

        val cacheTweets = this.tweetRepository.findByfromUser(screenName)
        val lastFetchedAt = cacheTweets.sortedByDescending { it.fetchedAt }.elementAtOrNull(0)?.fetchedAt
        if (lastFetchedAt != null && Date().time - lastFetchedAt.time < 604800000) { // 1 week
            this.logger.debug("fetched $screenName tweets from cache")
            return Observable.fromIterable(cacheTweets)
                    .sorted { t1, t2 -> t1.createdAt?.compareTo(t2.createdAt) ?: 0 }
                    .takeLast(limit)
                    .map { it.retweedFromScreenName ?: it.replyToScreenName ?: "" }
                    .filter { it != "" }
                    .distinct()
                    .map { Pair(screenName, it) }
        } else {
            this.logger.debug("fetched $screenName tweets from live")

            val creationDate = Date()
            return Observable.fromIterable(this.twitterService.getUserTimelineForScreenName(screenName))
                    .filter { it.retweetedStatus != null || it.inReplyToScreenName != "null" }
                    .sorted { t1, t2 -> t1.createdAt.compareTo(t2.createdAt) }
                    .takeLast(limit)
                    .doOnNext {
                        this.tweetSaverService.saveTweet(
                                Tweet(
                                        id = it.id,
                                        fromUser = it.fromUser,
                                        createdAt = it.createdAt,
                                        replyToScreenName = it.inReplyToScreenName,
                                        retweedFromScreenName = it.retweetedStatus?.fromUser,
                                        fetchedAt = creationDate
                                ))
                    }
                    .map { it.retweetedStatus?.fromUser ?: it.inReplyToScreenName ?: "" }
                    .filter { it != "" }
                    .distinct()
                    .map { Pair(screenName, it) }
        }
    }
}