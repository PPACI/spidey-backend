package io.spidey.Services

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.spidey.Models.Node
import io.spidey.Models.SigmaJsGraph
import io.spidey.Models.TwitterUser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.social.twitter.api.SearchParameters
import org.springframework.social.twitter.api.Tweet
import org.springframework.social.twitter.api.TwitterProfile
import org.springframework.social.twitter.api.impl.TwitterTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class GraphService {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass.name)
    @Autowired
    lateinit var relationService : RelationService



    /**
     * It returns an array of tweets from the selected screenName including retweets and replies.
     * In case of retweet, we could extract the original tweet then the author.
     * In case of reply, we could directly extract the author.
     * A function to do this job depending on retweet/reply status and returning a user_name should be done.
     */
    fun buildGraph(screenName: String): Single<SigmaJsGraph> {
        val start = Date()

        val firstLevel = this.relationService.getPairsOfRelation(screenName,10)

        val secondLevel = firstLevel
                .flatMap { pair -> this.relationService.getPairsOfRelation(pair.second,10) }
10
        val thirdLevel = secondLevel
                .flatMap { pair -> this.relationService.getPairsOfRelation(pair.second,10) }

        return Observable.merge(firstLevel, secondLevel, thirdLevel)
                .distinct()
                .map { Pair(Node(it.first), Node(it.second)) }
                .reduceWith({ SigmaJsGraph() }, { graph, (first, second) -> graph.addRelation(sourceNode = first, targetNode = second) })
                .map { it.trimMonoEdgeUser() }
                .doOnSuccess { graph ->
                    val end = Date()
                    val elapsedSeconds = (end.time - start.time) / 1000

                    logger.info("[buildGraph] time for graph generation $elapsedSeconds seconds")
                    logger.info("[getUserGraph] Graph results: ${graph.nodes.size} nodes and ${graph.edges.size} edges")
                }

    }

}