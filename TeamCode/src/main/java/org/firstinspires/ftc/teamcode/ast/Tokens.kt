package org.firstinspires.ftc.teamcode.ast

import org.firstinspires.ftc.teamcode.ast.Keyword
import org.firstinspires.ftc.teamcode.ast.Operator
import org.firstinspires.ftc.teamcode.ast.Punctuation
import kotlin.toString

sealed interface Token{
    val symbol: String
}
sealed class Keyword(override val symbol: String): Token {
    class Class: Keyword("class")
    class Fun: Keyword("fun")
    class Override: Keyword("override")
    class Return: Keyword("return")
    class Var: Keyword("var")
    class Val: Keyword("val")
    class For: Keyword("For")
    class While: Keyword("While")
    companion object {
        val allKeywords get() = listOf(
            Class(),
            Fun(),
            Override(),
            Return(),
            Var(),
            Val(),
            For(),
            While(),
        )
    }
}
sealed class Operator(override val symbol: String): Token {
    class Plus: Operator("+")
    class Minus: Operator("-")
    class Mul: Operator("*")
    class Div: Operator("/")
    class Rem: Operator("%")
    class And: Operator("&&")
    class Or: Operator("||")
    class Not: Operator("!")
    class Assignment: Operator("=")
    class PlusAssign: Operator("+=")
    class MinusAssign: Operator("-=")
    class MulAssign: Operator("*=")
    class DivAssign: Operator("/=")
    class RemAssign: Operator("%=")
    class Increment: Operator("++")
    class Decrement: Operator("--")
    companion object {
        val allOperators get() = listOf(
            Plus(),
            Minus(),
            Mul(),
            Div(),
            Rem(),
            And(),
            Or(),
            Not(),
            Assignment(),
            PlusAssign(),
            MinusAssign(),
            MulAssign(),
            DivAssign(),
            RemAssign(),
            Increment(),
            Decrement(),
        )
    }
}
sealed class Literal<T>: Token {
    abstract val value: T
    override val symbol get() = value.toString()

    class Int(
        override val value: kotlin.Int
    ): Literal<kotlin.Int>()

    class Double(
        override val value: kotlin.Double
    ): Literal<kotlin.Double>()

    class Boolean(
        override val value: kotlin.Boolean
    ): Literal<kotlin.Boolean>()

    class Char(
        override val value: kotlin.Char
    ): Literal<kotlin.Char>(){
        override val symbol = "\'" + value.toString() + "\'"
    }

    class String(
        override val value: kotlin.String
    ): Literal<kotlin.String>(){
        override val symbol = "\"" + value.toString() + "\""
    }

}

class Identifier(val name: String): Token { override val symbol = name }

sealed class Punctuation(override val symbol: String): Token {
    class Dot: Operator(".")
    class Colon: Operator(":")
    class Comma: Operator(",")
    class Semicolon: Punctuation(";")
    class Arrow: Operator("->")
    class LeftCurl: Punctuation("{")
    class RightCurl: Punctuation("}")
    class LeftParen: Punctuation("(")
    class RightParen: Punctuation(")")
    class LeftSquare: Punctuation("[")
    class RightSquare: Punctuation("]")
    class LeftAngle: Punctuation("<")
    class RightAngle: Punctuation(">")
    class LineComment: Punctuation("//")
    class BlockCommentOpen: Punctuation("/*")
    class BlockCommentClose: Punctuation("*/")
    companion object {
        val allPunctuation get() = listOf(
            Dot(),
            Colon(),
            Comma(),
            Semicolon(),
            Arrow(),
            LeftCurl(),
            RightCurl(),
            LeftParen(),
            RightParen(),
            LeftSquare(),
            RightSquare(),
            LeftAngle(),
            RightAngle(),
            LineComment(),
            BlockCommentOpen(),
            BlockCommentClose(),
        )
    }
}