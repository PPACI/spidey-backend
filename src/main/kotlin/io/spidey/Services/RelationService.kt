package io.spidey.Services

import io.reactivex.Observable
import io.spidey.Models.Tweet
import io.spidey.repository.TweetRepository
import io.spidey.repository.TwitterUserRepository
import org.springframework.stereotype.Service
import java.util.*


@Service
class RelationService(val twitterService: TwitterService, val tweetRepository: TweetRepository) {
    fun getPairsOfRelation(screenName: String): Observable<Pair<String, String>> {
        val cacheTweets = this.tweetRepository.findByfromUser(screenName)
        val lastFetchedAt = cacheTweets.sortedByDescending { it.fetchedAt }.elementAtOrNull(0)?.fetchedAt
        if (lastFetchedAt != null && Date().time - lastFetchedAt.time < 604800000) { // 1 week
            return Observable.fromIterable(cacheTweets)
                    .map { it.retweedFromScreenName ?: it.replyToScreenName ?: "" }
                    .filter { it != "" }
                    .distinct()
                    .map { Pair(screenName, it) }
        } else {
            val creationDate = Date()
            return Observable.fromIterable(this.twitterService.getUserTimelineForScreenName(screenName))
                    .filter { it.retweetedStatus != null || it.inReplyToScreenName != null }
                    .doOnNext {
                        this.tweetRepository.save(
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