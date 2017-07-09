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

    companion object {
        val logger: Logger = LoggerFactory.getLogger(GraphService::class.java.name)
    }

    @Autowired
    lateinit var relationService: RelationService

    private fun getRelations(fromNode: Node, level: Int): Observable<Pair<Node, Node>> {
        return this.relationService.getPairsOfRelation(fromNode, 15, level)
    }

    /**
     * It returns an array of tweets from the selected screenName including retweets and replies.
     * In case of retweet, we could extract the original tweet then the author.
     * In case of reply, we could directly extract the author.
     * A function to do this job depending on retweet/reply status and returning a user_name should be done.
     */
    fun buildGraph(screenName: String): Single<SigmaJsGraph> {
        val start = Date()
        val initialNode = Node(label = screenName, color = RelationService.levelColors[1]!!)

        val firstLevel = this.getRelations(initialNode, level = 1)
                .doOnComplete { logger.debug("finished lvl 1") }


        val secondLevel = firstLevel
                .flatMap {
                    Observable.just(it)
                            .subscribeOn(Schedulers.io())
                            .flatMap { pair -> this.getRelations(pair.second, level = 2) }
                }
                .doOnComplete { logger.debug("finished lvl 2") }

        val thirdLevel = secondLevel
                .flatMap {
                    Observable.just(it)
                            .subscribeOn(Schedulers.io())
                            .flatMap { pair -> this.getRelations(pair.second, level = 3) }
                }
                .doOnComplete { logger.debug("finished lvl 3") }



        return Observable.merge(firstLevel, secondLevel, thirdLevel)
                .distinct()
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