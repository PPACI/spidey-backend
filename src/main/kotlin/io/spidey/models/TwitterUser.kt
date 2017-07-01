package io.spidey.models

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.util.*

@Document(indexName = "twitter", type = "user")
data class TwitterUser(@Id val id: String? = null,
                  val screenName: String? = null,
                  val description: String? = null,
                  val profilePictureUrl: String? = null,
                  val bannerPictureUrl: String? = null,
                  @Field(type = FieldType.Date) val lastUpdateDate: Date? = null)