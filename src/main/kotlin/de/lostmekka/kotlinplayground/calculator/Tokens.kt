package de.lostmekka.kotlinplayground.calculator

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
