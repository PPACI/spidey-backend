package io.spidey.repository

import io.spidey.Models.Tweet
import io.spidey.Models.TwitterUser
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface TwitterUserRepository : ElasticsearchRepository<TwitterUser, String>{
    fun findByUserName(fromUser: String): List<Tweet>
}
