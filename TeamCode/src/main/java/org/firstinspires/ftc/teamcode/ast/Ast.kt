package org.firstinspires.ftc.teamcode.ast

interface Node {
    val type: String
}

class Assign(val variable: Variable, val value: Node): Node {
    override val type = "Unit"
}

class Value(val value: Any, override val type: String): Node

class Variable(val name: String, override val type: String): Node

class SequentialExecution(val nodes: List<Node>): Node {
    override val type: String
        get() = nodes.lastOrNull()?.type ?: "Unit"
}
/**
 * represents receiver.name(*parameters)
 */
class FunctionCall(
    val receiver: Node,
    val name: String,
    override val type: String,
    var parameters: List<Variable>,
    val body: Node
): Node

class ClassNode(
    val name: String,
    val memberFunctions: MutableList<FunctionCall>
): Node {
    override val type = "Unit"
}