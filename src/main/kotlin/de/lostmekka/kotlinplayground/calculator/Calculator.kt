package de.lostmekka.kotlinplayground.calculator

class Calculator : ICalculator {
    val tokenPatterns = mutableListOf<Pair<TokenType, String>>()
    override fun evaluate(formula: String): Double {
        val tokens = tokenize(formula)
        if (tokens.isEmpty()) throw ParseException("the input is empty")
        val ast = parse(tokens)
        return ast.evaluate()
    }

    fun tokenize(formula: String): List<Token> {
        TODO()
    }

    fun parse(tokens: List<Token>): ASTNode {
        TODO()
    }
}
