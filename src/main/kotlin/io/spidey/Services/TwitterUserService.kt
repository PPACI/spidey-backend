package io.spidey.Services

import io.spidey.Models.TwitterUser
import io.spidey.repository.TwitterUserRepository
import org.springframework.social.twitter.api.TwitterProfile
import org.springframework.stereotype.Service

@Service
class TwitterUserService(val twitterUserRepository: TwitterUserRepository){


}
