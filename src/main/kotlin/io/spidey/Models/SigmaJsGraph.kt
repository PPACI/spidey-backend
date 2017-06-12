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
    val size: Float
    val color: String
    val x: Int
    val y: Int


    constructor(label: String, size: Float = 0.2F, color: String = "#444") {
        this.x = rng.nextInt(100)
        this.y = rng.nextInt(100)

        this.id = label // since the label is the unique twitter user_name
        this.label = label
        this.size = size
        this.color = color
    }

    constructor(label: String, size: Float = 0.2F, color: String = "#444", x: Int, y: Int){
        this.id = label
        this.label = label
        this.size = size
        this.color = color
        this.x = x
        this.y = y
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        when (other){
            is Node -> return this.id == other.id
            else -> return false
        }
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