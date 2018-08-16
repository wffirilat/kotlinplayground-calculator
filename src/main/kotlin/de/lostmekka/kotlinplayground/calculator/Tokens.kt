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
    Skip
}
