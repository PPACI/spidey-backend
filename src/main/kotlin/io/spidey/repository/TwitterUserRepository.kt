package io.spidey.repository

import io.spidey.models.TwitterUser
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface TwitterUserRepository : ElasticsearchRepository<TwitterUser, String> {

    fun findByScreenName(screenName: String): TwitterUser?

}
