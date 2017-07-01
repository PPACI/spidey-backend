package io.spidey.models

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.util.*

@Document(indexName = "twitter", type = "tweet")
class Tweet(@Id val id: String? = null,
            val fromUser: String? = null,
            val replyToScreenName: String? = null,
            val retweedFromScreenName: String? = null,
            @Field(type = FieldType.Date) val createdAt: Date? = null,
            @Field(type = FieldType.Date) val fetchedAt: Date? = null)