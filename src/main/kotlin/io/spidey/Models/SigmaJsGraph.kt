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

    fun contains(node: Node):Boolean = arrayOf(source, target).contains(node.id)

}

/**
 * A SigmaJsGraph is the SigmaJS data format model, composed of nodes and edges
 */
class SigmaJsGraph() {

    val nodes = HashSet<Node>()
    val edges = HashSet<Edge>()

    constructor(nodeSet:HashSet<Node>, edgeSet:HashSet<Edge>):this(){
        this.nodes.addAll(nodeSet)
        this.edges.addAll(edgeSet)
    }

    fun addRelation(sourceNode:Node, targetNode: Node):SigmaJsGraph{
        this.nodes.addAll(arrayOf(sourceNode,targetNode))
        val edge = Edge(sourceNode = sourceNode, targetNode = targetNode)
        this.edges.add(edge)
        return this
    }

    fun countEdgesForNode(node: Node):Int = this.edges.filter { it.contains(node) }.count()

    /**
     * Trim all node belonging to only one edge
     * @return A trimmed SigmaJsGraph
     */
    fun trimMonoEdgeUser(): SigmaJsGraph{
        val validNodes = this.nodes.filter { this.countEdgesForNode(it) > 1 }.toHashSet()
        val validNodesId = validNodes.map { it.id }
        val validEdges = this.edges.filter { validNodesId.contains(it.source) && validNodesId.contains(it.target) }.toHashSet()
        return SigmaJsGraph(nodeSet = validNodes, edgeSet = validEdges)
    }

}