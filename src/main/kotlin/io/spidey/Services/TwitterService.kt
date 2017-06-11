package io.spidey.Services

import io.reactivex.Flowable
import io.spidey.Models.Node
import io.spidey.Models.SigmaJsGraph
import org.springframework.social.twitter.api.SearchParameters
import org.springframework.social.twitter.api.Tweet
import org.springframework.social.twitter.api.TwitterProfile
import org.springframework.social.twitter.api.impl.TwitterTemplate
import org.springframework.stereotype.Service

@Service
class TwitterService {
    val twitter = TwitterTemplate("qPe0o5axovP4VlGnv9bDchVwI", "yFWHo5mHwIy99jlTfgp2loSpnRlGLMwRvpNJqqFC6NQQfqal15")

    fun getHelloWorldTweets(number: Int): Flowable<Tweet> {
        return Flowable.fromIterable(this.twitter.searchOperations().search(
                SearchParameters("hello world").count(number)
        ).tweets)
    }

    fun getUserTimeline(user_id: String): Flowable<TwitterProfile> {
        return Flowable.just(this.twitter.userOperations().getUserProfile(user_id))
    }


    fun getUserGraph(screen_name: String): SigmaJsGraph {
        val graph = SigmaJsGraph()
        val first_level = Flowable.fromIterable(GetPairsOfRelation(screen_name))
                .take(100)
        val second_level = first_level.flatMap { pair -> Flowable.fromIterable(GetPairsOfRelation(pair.second)) }
                .take(300)
        val third_level = second_level.flatMap { pair -> Flowable.fromIterable(GetPairsOfRelation(pair.second)) }
                .take(500)

        Flowable.merge(first_level, second_level, third_level)
                .distinct()
                .map { Pair(Node(it.first), Node(it.second)) }
                .subscribe{graph.addRelation(sourceNode = it.first, targetNode = it.second)}
        return graph
    }

    fun GetPairsOfRelation(screen_name:String):List<Pair<String, String>>{
        return this.twitter.timelineOperations().getUserTimeline(screen_name,200)
                .map { it.retweetedStatus?.fromUser ?: it.inReplyToScreenName}
                .filterNotNull()
                .distinct()
                .map { Pair(screen_name, it) }
    }
}