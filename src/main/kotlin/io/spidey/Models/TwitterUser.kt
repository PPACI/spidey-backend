package io.spidey.Models

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import java.time.LocalDateTime

@Document(indexName = "twitter", type = "user")
class TwitterUser(@Id val id: String,
                  val screenName: String,
                  val description: String,
                  val profilePictureUrl: String,
                  val bannerPictureUrl: String,
                  val lastUpdateDate: LocalDateTime)