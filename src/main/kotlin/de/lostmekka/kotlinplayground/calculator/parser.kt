package de.lostmekka.kotlinplayground.calculator

import de.lostmekka.kotlinplayground.calculator.Operator.*
import de.lostmekka.kotlinplayground.calculator.TokenType.*
import de.lostmekka.kotlinplayground.calculator.TokenType.Number

typealias PrefixParselet = TokenStream.(Token) -> ASTNode
typealias InfixParselet = TokenStream.(Token, ASTNode) -> ASTNode

object Parser {
    private val prefixParsers = mutableMapOf<TokenType, PrefixParselet>()
    private val infixParsers = mutableMapOf<TokenType, InfixParselet>()
    private val precedences = mutableMapOf<TokenType, Int>()

    fun prefix(typ: TokenType, handler: PrefixParselet) {
        if (typ in prefixParsers) throw Exception("A prefix parselet for ${typ.name} is already defined.")
        prefixParsers[typ] = handler
    }

    fun infix(typ: TokenType, precedence: Int, handler: InfixParselet) {
        if (typ in infixParsers) throw Exception("An infix parselet for ${typ.name} is already defined.")
        infixParsers[typ] = handler
        precedences[typ] = precedence
    }

    fun parse(tokens: List<Token>): ASTNode {
        val stream = TokenStream(tokens)
        return stream.parseNext().also {stream.expect(EOF)}
    }

    fun TokenStream.parseNext(precedence: Int = 0): ASTNode {
        val handler = prefixParsers[peek().type]
                ?: throw ParseException("syntax error, unexpected token ${peek().type.name}")
        var ast = this.handler(pop())
        while (peek().type in infixParsers) {
            val parser = infixParsers[peek().type]!!
            val prec = precedences[peek().type]!!
            if (prec > precedence)
                ast = this.parser(pop(), ast)
            else break
        }
        return ast
    }
}

fun parserGrammar(initBlock: Parser.() -> Unit): (List<Token>) -> ASTNode {
    return Parser.also(initBlock)::parse
}
