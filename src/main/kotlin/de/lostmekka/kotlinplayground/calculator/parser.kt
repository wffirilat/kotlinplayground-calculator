package de.lostmekka.kotlinplayground.calculator

import de.lostmekka.kotlinplayground.calculator.TokenType.*
import de.lostmekka.kotlinplayground.calculator.TokenType.Number

typealias PrefixParselet = TokenStream.(Token) -> ASTNode
typealias InfixParselet = TokenStream.(Token, ASTNode) -> ASTNode

object Parser {
    private val prefixParsers = mutableMapOf<TokenType, PrefixParselet>()
    private val infixParsers = mutableMapOf<TokenType, InfixParselet>()

    fun prefix(typ: TokenType, handler: PrefixParselet) {
        if (typ in prefixParsers) throw Exception("A prefix parselet for ${typ.name} is already defined.")
        prefixParsers[typ] = handler
    }

    fun infix(typ: TokenType, handler: InfixParselet) {
        if (typ in infixParsers) throw Exception("An infix parselet for ${typ.name} is already defined.")
        infixParsers[typ] = handler
    }

    fun parse(tokens: List<Token>): ASTNode {
        val stream = TokenStream(tokens)
        return stream.parseNext()
    }

    fun TokenStream.parseNext(): ASTNode {
        val handler = prefixParsers[peek().type]
                ?: throw ParseException("syntax error, unexpected token ${peek().type.name}")
        var ast = this.handler(pop())
        var i = 0
        while (peek().type in infixParsers) {
            val parser = infixParsers[peek().type]!!
            ast = this.parser(pop(), ast)
            if (i > 5) {
                print("")
            }
            i++
        }
        return ast
    }

    init {
        prefix(OpenParen) {
            return@prefix parseNext().also { expect(CloseParen) }
        }
        prefix(Number) { token -> Value(token.text.toDouble()) }

        prefix(OperatorSub) { token -> TODO() }

        infix(OperatorAdd) { token, left -> TODO() }
        infix(OperatorMul) { token, left -> TODO() }
        infix(OperatorSub) { token, left -> TODO() }
        infix(OperatorDiv) { token, left -> TODO() }
    }
}
