package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ClassUtil.invoke
import com.sun.tools.javac.jvm.Gen.one
import org.firstinspires.ftc.teamcode.OpCodes.CALL
import org.firstinspires.ftc.teamcode.OpCodes.CONDJMP
import org.firstinspires.ftc.teamcode.OpCodes.FUNCTION
import org.firstinspires.ftc.teamcode.OpCodes.LOAD
import org.firstinspires.ftc.teamcode.OpCodes.RETURN
import java.io.File
import kotlin.reflect.full.functions


class OpMode: LinearOpMode() {
    // one line
    val stack = ArrayDeque<Int>()
    var head = 0
    val registers = mutableMapOf<String, Any?>(
        "this" to this,
        "hardwareMap" to this.hardwareMap,
        "null" to null,
        "true" to true,
        "false" to false,
        "1.0" to 1.0
    )
    lateinit var text: List<String>

    override fun runOpMode() {
        println("started")

        // zero lines
        text = File("text.txt").readLines()

        // one line
        for(i in 0..5 /*generateSequence {
            if(isStopRequested ) null else 0.0
        }*/){
            // one line
            val params = text[head].slice(
                2..< text[head].length
            ).split("%")
            // one line
            when(text[head][0]){

                // one line
                CALL -> {
                    val source = params[0]
                    val dest = params[1]
                    val method = params[2]

                    print("params: ")
                    params.forEach {
                        print(it.toString() + "(${it::class}), ")
                    }
                    println()
                    registers[dest] = (
                        registers[source]!!::class.java.methods
                        .first {
                             it.name == method
                        }.invoke(
                            registers[source]!!,
                            *params.slice(3..<params.size).toTypedArray()
                        )
                    )
                    head ++
                }

                // one line
                LOAD -> {
                    registers[params[0]] = params[1]
                    head ++
                }

                // one line
                RETURN -> {
                    head = stack.removeLastOrNull() ?: break
                }

                // one line
                CONDJMP -> {
                    if (registers[params[0]] as Boolean) {
                        head = params[1].toInt()
                    } else head ++
                }
                // one line
                FUNCTION -> {
                    stack.addLast(head)
                    head = params[0].toInt()
                }
            }
            println(registers)
            println("head: $head")
        }
    }
}