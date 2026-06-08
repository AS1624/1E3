package org.firstinspires.ftc.teamcode.ast

import java.io.File

class Tokenize(val file: File){
    val text = file.readText()
    var head = 0
    var parenthesisLevel = 0
    val tokens = mutableListOf<Token>()
    fun tokenize() {
        mainLoop@while (head < text.length) {
            if(parenthesisLevel == 0){
                var index = head
                findNext@while(index < text.length){
                    if(text[index] == '\n'){
                        tokens.add(Punctuation.Semicolon())
                        break@findNext
                    }
                    if(!text[index].isWhitespace()) break@findNext
                    index ++
                }
            }
            trim()

            if(tryMatch(Punctuation.LineComment()) != null){
                readLine()
                continue;
            }
            if(tryMatch(Punctuation.BlockCommentOpen()) != null){

                var commentLevel = 1
                while(commentLevel > 0 && head < text.length){
                    if(tryMatch(Punctuation.BlockCommentOpen()) != null){
                        commentLevel ++
                    }
                    else if(tryMatch(Punctuation.BlockCommentClose()) != null){
                        commentLevel --
                    }
                    else advance()
                }
                continue

            }
            Keyword.allKeywords.forEach { keyword ->
                tryMatch(keyword)?.let {
                    tokens.add(it)
                    continue@mainLoop
                }
            }
            Operator.allOperators.forEach { operator ->
                tryMatch(operator)?.let {
                    tokens.add(it)
                    continue@mainLoop
                }
            }
            Punctuation.allPunctuation.forEach { punctuation ->
                tryMatch(punctuation)?.let {
                    tokens.add(it)
                    if(it is Punctuation.LeftParen){
                        parenthesisLevel ++
                    }
                    if(it is Punctuation.RightParen){
                        parenthesisLevel --
                    }
                    continue@mainLoop
                }
            }

            listOf(
                Literal.Boolean(true),
                Literal.Boolean(false)
            ).forEach { boolean ->
                tryMatch(boolean)?.let {
                    tokens.add(it)
                    continue@mainLoop
                }
            }

            if(text[head] == '"'){
                advance()
                var string = ""
                var escaped = false
                while(true){
                    if(escaped){
                        val next = nextChar()
                        string += when(next){
                            '\\' -> '\\'
                            'n'  -> '\n'
                            'r'  -> '\r'
                            't'  -> '\t'
                            '\'' -> '\''
                            '\"' -> '\"'
                            else -> throw IllegalArgumentException(
                                "char escape cannot be \\$next "
                                + "(#$head in $file)"
                            )
                        }
                        escaped = false
                    }
                    else {
                        val next = nextChar()
                        if(next == '\"'){
                            tokens.add(Literal.String(string))
                            continue@mainLoop
                        }
                        else if(next == '\\') escaped = true
                        else if(next == '\n') throw IllegalArgumentException(
                            "string must be terminated by a \" before EOL, "
                            + "got \"$string\" "
                            + "(#$head in $file)"
                        )
                        else string += next
                    }
                }
            }
            if(text[head] == '-' || text[head].isDigit()){
                var string = "" + nextChar()
                var isDouble = false
                while(true){
                    val next = text[head]
                    if(next.isDigit()) {
                        string += next
                        advance()
                    }
                    else if(next == '.'){
                        if(isDouble){
                            tokens.add(Literal.Double(string.toDouble()))
                            continue@mainLoop
                        }
                        else {
                            isDouble = true
                            advance()
                        }
                    }
                    else {
                        if(isDouble) {
                            tokens.add(Literal.Double(string.toDouble()))
                        }
                        else {
                            tokens.add(Literal.Int(string.toInt()))
                        }
                        continue@mainLoop
                    }
                }
            }
            if(text[head] == '\''){
                advance()
                val char1 = nextChar()
                tokens.add(Literal.Char(
                    if(char1 == '\\') {
                        val char2 = nextChar()
                        when (char2) {
                            '\\' -> '\\'
                            'n' -> '\n'
                            'r' -> '\r'
                            't' -> '\t'
                            '\'' -> '\''
                            '\"' -> '\"'
                            else -> throw IllegalArgumentException(
                                "char escape cannot be \\$char2 "
                                + "(#$head in $file)"
                            )
                        }
                    }
                    else char1
                ))
                continue
            }
            else {
                var string = ""
                while(true){
                    val next = text[head]
                    if(next.isLetterOrDigit()){
                        string += next
                        advance()
                    }
                    else {
                        if(string.isEmpty()) throw IllegalArgumentException(
                            "identifier cannot be empty. "
                            + "(#$head in $file)"
                        )
                        tokens.add(Identifier(string))
                        continue@mainLoop
                    }
                }
            }

        }
    }

    fun advance() = head ++
    fun nextChar() = text[head ++]

    fun trim(){
        val start = (head..<text.length).firstOrNull {
            !text[it].isWhitespace()
        }
        if(start != null){
            head = start
        }
    }
    fun tryMatch(token: Token) = (
        if(text.indexOf(token.symbol, head) == head){
            head += token.symbol.length
            token
        }
        else null
    )
    fun nextWordIs(word: String) =
        text.substring(
            head,
            head + word.length
        ) == word

    fun readLine(): String {
        val oldHead = head
        val end = (head..<text.length).firstOrNull {
            text[it] == '\n'
        }
        if(end == null) return ""

        head = end + 1
        return text.substring(
            oldHead,
            end - 1
        )
    }
}