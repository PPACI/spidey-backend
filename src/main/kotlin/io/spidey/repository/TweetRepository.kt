package io.spidey.repository

import io.spidey.Models.Tweet
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface TweetRepository : ElasticsearchRepository<Tweet, String>{
    fun findByfromUser(fromUser: String): List<Tweet>
}
