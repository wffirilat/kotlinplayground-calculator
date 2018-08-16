package de.lostmekka.kotlinplayground.calculator

sealed class ASTNode {
    abstract fun evaluate(): Double
}

enum class Operator {
    Add,
    Mul,
    Div,
    Sub
}

class BinaryOperator(val op: Operator, val left: ASTNode, val right: ASTNode) : ASTNode() {
    override fun evaluate(): Double {
        TODO()
    }
}

class UnaryOperator(val op: Operator, val value: ASTNode) : ASTNode() {
    override fun evaluate(): Double {
        TODO()
    }
}

class Value(val value: Double) : ASTNode() {
    override fun evaluate(): Double {
        TODO()
    }
}
