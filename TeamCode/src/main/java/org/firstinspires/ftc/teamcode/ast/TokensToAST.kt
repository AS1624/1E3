package org.firstinspires.ftc.teamcode.ast


class TokensToAST(val tokens: List<Token>) {
    var head = 0
    fun makeAST(){
        while(
               tokens[head] == Identifier("package")
            || tokens[head] == Identifier("import")
        ) {
            while(nextToken() != Punctuation.Semicolon()){ }
            head ++
        }

        if(nextToken() != Keyword.Class()){
            throw IllegalStateException(
                "you can only define classes at top level "
                + "( got ${tokens[head]} )"
            )
        }
        val clazz = ClassNode(
            (nextToken() as Identifier).name,
            mutableListOf<FunctionCall>()
        )
        if(tokens[head] is Punctuation.LeftParen){
            clazz.memberFunctions.add(FunctionCall(
                clazz,
                "constructor",
                clazz.name,
                functionParameters(),
                SequentialExecution(listOf())
            ))
        }
        if(tokens[head] is Punctuation.Colon){
            while(tokens[head] !is Punctuation.LeftCurl) nextToken()
        }
        val bracketLevel = 1
        while(bracketLevel > 0){
            val next =
        }

    }
    fun nextToken() = tokens[head ++]
    fun functionParameters(): List<Variable> {
        if (nextToken() !is Punctuation.LeftParen) {
            throw IllegalStateException(
                "supposed to be finding function parameters, no paren"
                        + "( got ${tokens[head - 1]} )"
            )
        }
        val parameters = mutableListOf<Variable>()

        var parenthesisLevel = 0
        parametersLoop@ while (parenthesisLevel >= 0) {
            val name = (nextToken() as Identifier).symbol
            nextToken() // :
            var type = ""
            val next = nextToken()
            if (next is Punctuation.LeftParen) {
                parenthesisLevel++
                type += next.symbol
            } else if (next is Punctuation.RightParen) {
                parenthesisLevel--
                if (parenthesisLevel >= 0) type += next.symbol
                else {
                    parameters.add(Variable(name, type))
                    break@parametersLoop
                }
            } else if (next is Punctuation.Comma) {
                parameters.add(Variable(name, type))

                if (tokens[head + 1] is Punctuation.RightParen) {
                    break@parametersLoop
                } else {
                    continue@parametersLoop
                }
            } else {
                type += next.symbol
            }
        }
        nextToken()
        return parameters
    }
}