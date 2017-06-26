package io.spidey.Services

import io.reactivex.Observable
import io.spidey.repository.TweetRepository
import io.spidey.repository.TwitterUserRepository
import org.springframework.stereotype.Service
import java.util.*


@Service
class RelationService(val twitterService: TwitterService, val tweetRepository: TweetRepository) {
    fun getPairsOfRelation(screenName: String): Observable<Pair<String, String>> {
        val cacheTweets = this.tweetRepository.findByfromUser(screenName)
        val lastFetchedAt = cacheTweets.sortedByDescending { it.fetchedAt }[0].fetchedAt
        if (cacheTweets.isNotEmpty() && lastFetchedAt != null && Date().time - lastFetchedAt.time < 604800000) { // 1 week
            return Observable.fromIterable(cacheTweets)
                    .map { it.retweedFromScreenName ?: it.replyToScreenName ?: "" }
                    .filter { it != "" }
                    .distinct()
                    .map { Pair(screenName, it) }
        } else {
            return Observable.fromIterable(this.twitterService.getUserTimelineForScreenName(screenName))
                    .map { it.retweetedStatus?.fromUser ?: it.inReplyToScreenName }
                    .distinct()
                    .map { Pair(screenName, it) }
            //TODO: add Tweets to cache
        }
    }
}