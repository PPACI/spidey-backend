package io.spidey.services

import io.reactivex.Single
import io.spidey.models.TwitterUser
import io.spidey.repository.TwitterUserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*


@Service
class ProfileService(val twitterService: TwitterService, val twitterUserRepository: TwitterUserRepository) {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass.name)

    fun getUserDetails(screenName: String): Single<TwitterUser> {
        val user = this.twitterUserRepository.findByScreenName(screenName)
        if (user != null && user.lastUpdateDate != null && Date().time - user.lastUpdateDate.time < 2629746000){ //1 month
            this.logger.debug("user: $screenName fetched from cache")
            return Single.just(user)
        }
        else{
            this.logger.debug("user: $screenName fetched from live API")
            return this.twitterService.getTwitterProfileForScreenName(screenName)
                    .map { TwitterUser(id = it.id.toString(),
                            screenName = it.screenName,
                            bannerPictureUrl = it.profileBannerUrl,
                            profilePictureUrl = it.profileImageUrl,
                            description = it.description,
                            lastUpdateDate = Date()) }.
                    doOnSuccess{this.twitterUserRepository.save(it)}
        }
    }
}