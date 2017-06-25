package io.spidey.Models


/**
 * A SigmaJsGraph is the SigmaJS data format model, composed of nodes and edges
 * @constructor Instantiate an empty SigmaJsGraph
 */
class SigmaJsGraph() {

    val nodes = HashSet<Node>()
    val edges = HashSet<Edge>()

    constructor(nodeSet: HashSet<Node>, edgeSet: HashSet<Edge>) : this() {
        this.nodes.addAll(nodeSet)
        this.edges.addAll(edgeSet)
    }

    fun addRelation(sourceNode: Node, targetNode: Node): SigmaJsGraph {
        this.nodes.addAll(arrayOf(sourceNode, targetNode))
        val edge = Edge(sourceNode = sourceNode, targetNode = targetNode)
        this.edges.add(edge)
        return this
    }

    fun countEdgesForNode(node: Node): Int = this.edges.filter { it.contains(node) }.count()

    /**
     * Trim all node belonging to only one edge
     * @return A trimmed SigmaJsGraph
     */
    fun trimMonoEdgeUser(): SigmaJsGraph {
        val validNodes = this.nodes.filter { this.countEdgesForNode(it) > 1 }.toHashSet()
        val validNodesId = validNodes.map { it.id }
        val validEdges = this.edges.filter { validNodesId.contains(it.source) && validNodesId.contains(it.target) }.toHashSet()
        return SigmaJsGraph(nodeSet = validNodes, edgeSet = validEdges)
    }

}