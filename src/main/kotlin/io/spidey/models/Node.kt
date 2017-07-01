package io.spidey.models

import java.util.*

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