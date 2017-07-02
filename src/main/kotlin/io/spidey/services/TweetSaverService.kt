package io.spidey.services

import io.reactivex.subjects.PublishSubject
import io.spidey.models.Tweet
import io.spidey.repository.TweetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TweetSaverService(@Autowired val tweetRepository: TweetRepository) {
    final val tweetStore: PublishSubject<Tweet> = PublishSubject.create<Tweet>()

    init {
        tweetStore.subscribe { this.tweetRepository.save(it) }
    }

    fun saveTweet(tweet: Tweet) = this.tweetStore.onNext(tweet)

}