package org.firstinspires.ftc.teamcode

import org.firstinspires.ftc.teamcode.ast.Tokenize
import java.io.File

fun main(){
    val tokenizer = Tokenize(File(
        "TeamCode/src/main/java/org/firstinspires/ftc/teamcode"
        + "/OpModeToCompile.kt"
    ))

    tokenizer.tokenize()
    tokenizer.tokens.forEach { print(it.symbol + " ") }
}