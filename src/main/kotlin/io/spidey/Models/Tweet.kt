package io.spidey.Models

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import java.util.*

@Document(indexName = "twitter", type = "tweet")
class Tweet(@Id val id: String,
            val createdAt: Date,
            val fromUser: String,
            val replyToScreenName: String,
            val retweedFromScreenName: String)