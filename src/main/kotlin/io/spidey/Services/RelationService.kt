package io.spidey.Services

import io.reactivex.Observable
import io.spidey.repository.TweetRepository
import org.springframework.stereotype.Service


@Service
class RelationService(val twitterService: TwitterService, val tweetRepository: TweetRepository) {
    fun getPairsOfRelation(screenName: String): Observable<Pair<String, String>> {
        //TODO: IMPLEMENT CACHE METHOD HERE
        return Observable.fromIterable(this.twitterService.getUserTimelineForScreenName(screenName))
                .map { it.retweetedStatus?.fromUser ?: it.inReplyToScreenName }
                .distinct()
                .map { Pair(screenName, it) }
    }
}