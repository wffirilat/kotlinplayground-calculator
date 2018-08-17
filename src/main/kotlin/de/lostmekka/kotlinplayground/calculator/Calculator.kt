package de.lostmekka.kotlinplayground.calculator

import de.lostmekka.kotlinplayground.calculator.TokenType.*
import de.lostmekka.kotlinplayground.calculator.TokenType.Number

class Calculator : ICalculator {
    val tokenPatterns = mutableListOf<Pair<TokenType, Regex>>()
    override fun evaluate(formula: String): Double {
        val tokens = tokenize(formula)
        if (tokens.isEmpty()) throw ParseException("the input is empty")
        val ast = parse(tokens)
        return ast.evaluate()
    }

    /**
     * Register the pattern to the TokenType. calling order matters.
     */
    private fun pattern(typ: TokenType, patt: String) {
        tokenPatterns.add(typ to patt.toRegex())
    }

    init {
        pattern(OpenParen, """\(""")
        pattern(CloseParen, """\)""")
        pattern(OperatorAdd, """\+""")
        pattern(OperatorMul, """\*""")
        pattern(OperatorSub, """\-""")
        pattern(OperatorDiv, """\/""")
        pattern(Number, """[0-9\.]+""")
        pattern(Skip, """\ +""")
    }

    fun tokenize(formula: String): List<Token> {
        val l = mutableListOf<Token>()
        var i = 0

        loop@ while (i < formula.length) {
            inner@ for ((typ, regex) in tokenPatterns) {
                val match = regex.find(formula, i)?.value ?: continue@inner
                if (!formula.regionMatches(i, match, 0, match.length)) continue@inner
                l.add(Token(match, typ))
                i += match.length
                continue@loop
            }
            break@loop
        }
        return l.toList()
    }

    fun parse(tokens: List<Token>): ASTNode {
        TODO()
    }
}
