package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ClassUtil.invoke
import java.io.File
import kotlin.reflect.full.functions

const val LOAD     = 'L' // L%reg dest%string%
const val CALL     = 'C' // C%reg source%%dest%%name%<% reg value% ... >%null%
const val CONDJMP  = 'J' // J%reg bool%%char# true%
const val FUNCTION = 'F' // F%char# function start%
const val RETURN   = 'R' // R

class OpMode: LinearOpMode() {
    /*
     * one line
     */
    val stack = ArrayDeque<Int>()
    var head = 0
    val registers = mutableMapOf<String, Any?>(
        //"this" to this,
        //"hardwareMap" to this.hardwareMap,
        "null" to null,
        "true" to true,
        "false" to false,
        "1.0" to 1.0
    )
    lateinit var text: String

    override fun runOpMode() {

        // waitForStart()

        println("started")

        // zero lines
        text = File("text.txt").readText()

        // one line
        for(i in 0..5 /*generateSequence {
            if(isStopRequested ) null else 0.0
        }*/){
            // zero lines
            println(text[head])
            when(text[head]){

                // two lines
                CALL -> {
                    val source = registers[next()]!!
                    val dest = next()
                    val name = next()
                    val method = source::class.java.methods.first {
                        it.name == name
                    }
                    val params = (generateSequence<Any> {
                        registers[next()]
                    }.toList().toTypedArray())

                    print("params: ")
                    params.forEach {
                        print(it.toString() + "(${it::class}), ")
                    }
                    println()
                    registers[dest] = method.invoke(
                        source, *params
                    )
                }

                // one line
                LOAD -> registers[next()] = next()

                RETURN -> {
                    head = stack.removeLastOrNull() ?: break
                }

                // one line
                CONDJMP -> {
                    if (registers[next()] as Boolean) {
                        head = next().toInt()
                    } else next()
                }
                // one line
                FUNCTION -> {
                    stack.addLast(head)
                    head = next().toInt()
                }
            }
            println(registers)
            println("head: $head")
        }
    }

    /*
     * 2 lines
     */
    fun next(): String {
        val end = text.indexOf("%", head + 1)
        //println("end: $end")
        val oldHead = head
        head = end + 1
        val output = text.slice(oldHead + 1 ..< end)
        println("next: $output")
        return output
    }
}