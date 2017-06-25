package io.spidey.Services

import io.reactivex.Single
import io.spidey.Models.TwitterUser
import io.spidey.repository.TwitterUserRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.util.*


@Service
class ProfileService(val twitterService: TwitterService, val twitterUserRepository: TwitterUserRepository) {
    fun getUserDetails(screenName: String): Single<TwitterUser> {
        val user = this.twitterUserRepository.findByScreenName(screenName)
        if (user != null){
            return Single.just(user)
        }
        else{
            return this.twitterService.getTwitterProfileForScreenName(screenName)
                    .map { TwitterUser(id = it.id.toString(),
                            screenName = it.screenName,
                            bannerPictureUrl = it.profileBannerUrl,
                            profilePictureUrl = it.profileImageUrl,
                            description = it.description,
                            lastUpdateDate = LocalDateTime.now()) }.
                    doOnSuccess{this.twitterUserRepository.save(it)}
        }
    }
}