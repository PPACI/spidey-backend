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

    companion object {
        val logger: Logger = LoggerFactory.getLogger(ProfileService::class.java.name)

        val ONE_MONTH = 2629746000
    }

    fun getUserDetails(screenName: String): Single<TwitterUser> {
        val user = this.twitterUserRepository.findByScreenName(screenName)
        if (userExistsAndLessThan(user, ONE_MONTH)) {
            logger.debug("user: $screenName fetched from cache")
            return Single.just(user)
        } else {
            logger.debug("user: $screenName fetched from live API")
            return this.twitterService.getTwitterProfileForScreenName(screenName)
                    .map {
                        TwitterUser(
                                id = it.id.toString(),
                                screenName = it.screenName,
                                bannerPictureUrl = it.profileBannerUrl,
                                profilePictureUrl = it.profileImageUrl,
                                description = it.description,
                                followerCount = it.followersCount,
                                lastUpdateDate = Date()
                        )
                    }
                    .doOnSuccess { this.twitterUserRepository.save(it) }
        }
    }

    fun userExistsAndLessThan(user: TwitterUser?, delay: Long): Boolean {
        return user != null && user.lastUpdateDate != null && Date().time - user.lastUpdateDate.time < ONE_MONTH
    }

}