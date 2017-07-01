package io.spidey.services

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.spidey.models.Node
import io.spidey.models.SigmaJsGraph
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class GraphService {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass.name)

    @Autowired
    lateinit var relationService: RelationService

    private fun getRelations(screenName: String): Observable<Pair<String, String>> {
        return this.relationService.getPairsOfRelation(screenName, 10)
    }

    /**
     * It returns an array of tweets from the selected screenName including retweets and replies.
     * In case of retweet, we could extract the original tweet then the author.
     * In case of reply, we could directly extract the author.
     * A function to do this job depending on retweet/reply status and returning a user_name should be done.
     */
    fun buildGraph(screenName: String): Single<SigmaJsGraph> {
        val start = Date()

        val firstLevel = this.getRelations(screenName)

        val secondLevel = firstLevel
                .flatMap {
                    Observable.just(it)
                        .subscribeOn(Schedulers.computation())
                        .flatMap { pair -> this.getRelations(pair.second) }
                }

        val thirdLevel = secondLevel
                .flatMap {
                    Observable.just(it)
                        .subscribeOn(Schedulers.computation())
                        .flatMap { pair -> this.getRelations(pair.second) }
                }

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