package io.spidey.Models

import java.util.*
import kotlin.collections.HashSet


/**
 * A node represent a point on a SigmaJS graph
 */
class Node {

    companion object {
        val rng = Random(0)
    }

    val id: String
    val label: String
    val size: Int
    val color: String
    val x: Int
    val y: Int


    constructor(label: String, size: Int = 1, color: String = "#000") {
        this.x = rng.nextInt() * 100
        this.y = rng.nextInt() * 100

        this.id = label + '-' + this.x + '-' + this.y // since the label is the unique twitter user_name
        this.label = label
        this.size = size
        this.color = color
    }

    constructor(label: String, size: Int = 1, color: String = "#000", x: Int, y: Int){
        this.id = label
        this.label = label
        this.size = size
        this.color = color
        this.x = x
        this.y = y
    }

}

/**
 * An Edge represent the link between two nodes in SigmaJS graph
 */
class Edge(sourceNode: Node, targetNode: Node) {
    val id = "${sourceNode.id}-${targetNode.id}"
    val source = sourceNode.id
    val target = targetNode.id
}

/**
 * A SigmaJsGraph is the SigmaJS data format model, composed of nodes and edges
 */
class SigmaJsGraph {
    val nodes = HashSet<Node>()
    val edges = HashSet<Edge>()

    fun addRelation(sourceNode:Node, targetNode: Node){
        this.nodes.addAll(arrayOf(sourceNode,targetNode))
        val edge = Edge(sourceNode = sourceNode, targetNode = targetNode)
        this.edges.add(edge)
    }
}