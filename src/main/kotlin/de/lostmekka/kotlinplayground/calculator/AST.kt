package de.lostmekka.kotlinplayground.calculator

import de.lostmekka.kotlinplayground.calculator.Operator.*

enum class Operator {
    Add,
    Mul,
    Div,
    Sub
}

sealed class ASTNode {
    abstract fun evaluate(): Double
}

class BinaryOperator(val op: Operator, val left: ASTNode, val right: ASTNode) : ASTNode() {
    override fun evaluate(): Double {
        val l = left.evaluate()
        val r = right.evaluate()
        return when (op) {
            Add -> l + r
            Mul -> l * r
            Div -> if (r == 0.0) throw EvaluateException("it is a division by zero")
            else return l / r
            Sub -> return l - r
        }
    }
}

class UnaryOperator(val op: Operator, val value: ASTNode) : ASTNode() {
    override fun evaluate(): Double {
        val v = value.evaluate()
        return when (op) {
            Sub -> -v
            else -> throw EvaluateException("$op is an invalid unary operator")
        }
    }
}

class Value(val value: Double) : ASTNode() {
    override fun evaluate(): Double {
        return value
    }
}
