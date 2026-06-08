package org.firstinspires.ftc.teamcode

object OpCodes {
    const val LOAD     = 'L' // L%reg dest%string%
    const val CALL     = 'C' // C%reg source%%dest%%name%<% reg value% ... >%null%
    const val CONDJMP  = 'J' // J%reg bool%%char# true%
    const val FUNCTION = 'F' // F%char# function start%
    const val RETURN   = 'R' // R
}