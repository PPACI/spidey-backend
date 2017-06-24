package io.spidey.Models

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "twitter", type = "user", shards = 1, replicas = 0)
class TwitterUser(@Id val id: String? = null, val userName: String,val description: String,val profilePictureUrl: String,val bannerPictureUrl: String)