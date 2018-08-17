package de.lostmekka.kotlinplayground.calculator

import de.lostmekka.kotlinplayground.calculator.TokenType.*
import de.lostmekka.kotlinplayground.calculator.TokenType.Number

val tokenize = tokenGrammar {
    pattern(TokenType.OpenParen, """\(""")
    pattern(TokenType.CloseParen, """\)""")
    pattern(TokenType.OperatorAdd, """\+""")
    pattern(TokenType.OperatorMul, """\*""")
    pattern(TokenType.OperatorSub, """\-""")
    pattern(TokenType.OperatorDiv, """\/""")
    pattern(TokenType.Number, """-?[0123456789\.]+(E-?[1-9][0-9]*)?""")
    pattern(TokenType.Skip, """\ +""")
}

val parse = parserGrammar {
    prefix(OpenParen) { return@prefix parseNext().also { expect(CloseParen) } }
    prefix(CloseParen) { throw ParseException("there is an opening parenthesis missing") }

    prefix(Number) { token -> Value(token.text.toDouble()) }

    prefix(OperatorSub) { UnaryOperator(Operator.Sub, parseNext(3)) }

    infix(OperatorAdd, 1) { _, left -> BinaryOperator(Operator.Add, left, parseNext(1)) }
    infix(OperatorMul, 2) { _, left -> BinaryOperator(Operator.Mul, left, parseNext(2)) }
    infix(OperatorSub, 1) { _, left -> BinaryOperator(Operator.Sub, left, parseNext(1)) }
    infix(OperatorDiv, 2) { _, left -> BinaryOperator(Operator.Div, left, parseNext(2)) }
}

class Calculator : ICalculator {
    override fun evaluate(formula: String): Double {
        val tokens = tokenize(formula)
        if (tokens.isEmpty()) throw ParseException("the input is empty")
        val ast = parse(tokens)
        return ast.evaluate()
    }
}
