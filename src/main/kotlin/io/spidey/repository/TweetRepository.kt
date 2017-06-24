package io.spidey.repository

import io.spidey.Models.TwitterUser
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.social.twitter.api.Tweet

interface TweetRepository: ElasticsearchRepository<TwitterUser, String>
