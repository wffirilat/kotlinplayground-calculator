package de.lostmekka.kotlinplayground.calculator

class Tokenizer {
    private val tokenPatterns = mutableListOf<Pair<TokenType, Regex>>()

    fun pattern(typ: TokenType, patt: String) {
        tokenPatterns.add(typ to patt.toRegex())
    }

    fun tokenize(formula: String): List<Token> {
        val l = mutableListOf<Token>()
        var i = 0

        loop@ while (i < formula.length) {
            inner@ for ((typ, regex) in tokenPatterns) {
                val match = regex.find(formula, i)?.value ?: continue@inner
                if (!formula.regionMatches(i, match, 0, match.length)) continue@inner
                if (typ == TokenType.Number && match.count { it == '.' } > 1)
                    throw ParseException("there are two decimal points")
                l.add(Token(match, typ))
                i += match.length
                continue@loop
            }
            break@loop
        }
        return l.toList()
    }
}

fun tokenGrammar(initBlock: Tokenizer.() -> Unit): (String) -> List<Token> {
    return Tokenizer().also(initBlock)::tokenize
}

class Token(val text: String, val type: TokenType)

enum class TokenType {
    OpenParen,
    CloseParen,
    OperatorAdd,
    OperatorMul,
    OperatorSub,
    OperatorDiv,
    Number,
    Skip,
    EOF
}

class TokenStream(tkns: List<Token>) {
    private val tokens = tkns.filter { tkn -> tkn.type != TokenType.Skip }
    private var i = 0
    val isFinished get() = i >= tokens.size

    fun peek(): Token {
        if (isFinished) return Token("", TokenType.EOF)
        return tokens[i]
    }

    fun pop(): Token {
        return peek().also { i++ }
    }

    fun expect(typ: TokenType): Token {
        val t = pop()
        if (t.type != typ) throw ParseException("Invalid syntax: expected ${typ.name}, got ${t.type.name}")
        return t
    }
}
