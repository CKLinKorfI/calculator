package com.example.calculator

import android.content.Context
import com.example.calculator.logics.Operations

class ExpressionEvaluator(
    private val context: Context
) {

    fun evaluate(expression: String): Double {
        val tokens = tokenize(expression)
        val afterMulDiv = processMultiplyDivide(tokens)
        return processAddSubtract(afterMulDiv)
    }

    private fun tokenize(expression: String): MutableList<String> {
        val result = mutableListOf<String>()
        val input = expression.replace(" ", "")
        val number = StringBuilder()

        for (i in input.indices) {
            val ch = input[i]

            if (ch.isDigit() || ch == '.') {
                number.append(ch)
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                if (number.isNotEmpty()) {
                    result.add(number.toString())
                    number.clear()
                }

                if (ch == '-' && (result.isEmpty() || isOperator(result.last()))) {
                    number.append(ch)
                } else {
                    result.add(ch.toString())
                }
            } else {
                throw IllegalArgumentException(
                    context.getString(R.string.error_invalid_symbol, ch.toString())
                )
            }
        }

        if (number.isNotEmpty()) {
            result.add(number.toString())
        }

        return result
    }

    private fun processMultiplyDivide(tokens: MutableList<String>): MutableList<String> {
        val result = mutableListOf<String>()
        var index = 0

        while (index < tokens.size) {
            val token = tokens[index]

            if (token == Operations.MULTIPLY.symbol || token == Operations.DIVIDE.symbol) {
                val left = result.removeAt(result.lastIndex).toDouble()
                val right = tokens[index + 1].toDouble()

                val value = when (token) {
                    Operations.MULTIPLY.symbol -> left * right
                    Operations.DIVIDE.symbol -> {
                        if (right == 0.0) {
                            throw ArithmeticException(
                                context.getString(R.string.error_division_by_zero)
                            )
                        }
                        left / right
                    }
                    else -> 0.0
                }

                result.add(value.toString())
                index += 2
            } else {
                result.add(token)
                index++
            }
        }

        return result
    }

    private fun processAddSubtract(tokens: MutableList<String>): Double {
        var result = tokens[0].toDouble()
        var index = 1

        while (index < tokens.size) {
            val operator = tokens[index]
            val number = tokens[index + 1].toDouble()

            result = when (operator) {
                Operations.ADD.symbol -> result + number
                Operations.SUBTRACT.symbol -> result - number
                else -> throw IllegalArgumentException(
                    context.getString(R.string.error_unknown_operation, operator)
                )
            }

            index += 2
        }

        return result
    }

    private fun isOperator(value: String): Boolean {
        return value == Operations.ADD.symbol ||
                value == Operations.SUBTRACT.symbol ||
                value == Operations.MULTIPLY.symbol ||
                value == Operations.DIVIDE.symbol
    }
}