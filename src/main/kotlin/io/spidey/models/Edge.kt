package io.spidey.models

/**
 * An Edge represent the link between two nodes in SigmaJS graph
 */
class Edge(sourceNode: Node, targetNode: Node) {

    val id = "${sourceNode.id}-${targetNode.id}"
    val source = sourceNode.id
    val target = targetNode.id

    fun contains(node: Node):Boolean = arrayOf(source, target).contains(node.id)

}