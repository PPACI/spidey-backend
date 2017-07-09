package io.spidey.services

import io.reactivex.Observable
import io.spidey.models.Node
import io.spidey.models.Tweet
import io.spidey.repository.TweetRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*


@Service
class RelationService(val twitterService: TwitterService, val tweetRepository: TweetRepository, val tweetSaverService: TweetSaverService) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(RelationService::class.java.name)

        val levelColors = mapOf(
            1 to "#3DC02B",
            2 to "#E69A2F",
            3 to "#CF280A"
        )
    }

    fun getPairsOfRelation(fromNode: Node, limit: Int, level: Int): Observable<Pair<Node, Node>> {

        val cacheTweets = this.tweetRepository.findByfromUser(fromNode.id)
        val lastFetchedAt = cacheTweets.sortedByDescending { it.fetchedAt }.elementAtOrNull(0)?.fetchedAt

        if (lastFetchedAt != null && Date().time - lastFetchedAt.time < 604800000) { // 1 week
            logger.debug("fetched $fromNode tweets from cache")

            return Observable.fromIterable(cacheTweets)
                    .sorted { t1, t2 -> t1.createdAt?.compareTo(t2.createdAt) ?: 0 }
                    .takeLast(limit)
                    .map { it.retweedFromScreenName ?: it.replyToScreenName ?: "" }
                    .filter { !it.isNullOrEmpty() }
                    .distinct()
                    .map{Node(
                            label = it,
                            color = levelColors[level]!!
                    )}
                    .map { Pair(fromNode, it) }

        } else {
            logger.debug("fetched $fromNode tweets from live")

            val creationDate = Date()

            return Observable.fromIterable(this.twitterService.getUserTimelineForScreenName(fromNode.id))
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
                            )
                        )
                    }
                    .map { it.retweetedStatus?.fromUser ?: it.inReplyToScreenName ?: "" }
                    .filter { it != "" }
                    .distinct()
                    .map{Node(
                        label = it,
                        color = levelColors[level]!!
                    )}
                    .map { Pair(fromNode, it) }
        }
    }
}