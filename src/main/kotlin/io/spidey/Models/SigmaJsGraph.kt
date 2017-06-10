package io.spidey.Models

import java.util.*
import kotlin.collections.HashSet


/**
 * A node represent a point on a SigmaJS graph
 */
class Node {
    val id: String
    val label: String
    val size: Int
    val color: String
    val x: Float
    val y: Float


    constructor(label: String, size: Int = 1, color: String = "#000") {
        this.id = label // since the label is the unique twitter user_name
        this.label = label
        this.size = size
        this.color = color

        val rng = Random(0)
        this.x = rng.nextFloat() * 64 - 32
        this.y = rng.nextFloat() * 64 - 32
    }


    constructor(label: String, size: Int = 1, color: String = "#000", x: Float, y: Float){
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

    fun AddRelation(sourceNode:Node, targetNode: Node){
        this.nodes.addAll(arrayOf(sourceNode,targetNode))
        val edge = Edge(sourceNode = sourceNode, targetNode = targetNode)
        this.edges.add(edge)
    }
}