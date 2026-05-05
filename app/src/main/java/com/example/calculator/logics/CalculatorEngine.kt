package com.example.calculator

import android.content.Context
import com.example.calculator.logics.CalculatorStates
import com.example.calculator.logics.Operations

class CalculatorEngine(
    private val context: Context,

) {
    private val evaluator: ExpressionEvaluator = ExpressionEvaluator(context)
    private var state = CalculatorStates()

    fun getState(): CalculatorStates {
        return state
    }

    fun inputDigit(digit: String) {
        if (state.isResultShown) {
            state = CalculatorStates(
                expression = digit,
                result = digit
            )
        } else {
            val newExpression = state.expression + digit
            state = state.copy(
                expression = newExpression,
                result = newExpression,
                errorMessage = null
            )
        }
    }

    fun inputDot() {
        val baseExpression = if (state.isResultShown) "" else state.expression
        val currentNumber = getCurrentNumber(baseExpression)

        if (currentNumber.contains(".")) {
            return
        }

        val dotText = if (currentNumber.isEmpty()) "0." else "."
        val newExpression = baseExpression + dotText

        state = state.copy(
            expression = newExpression,
            result = newExpression,
            errorMessage = null,
            isResultShown = false
        )
    }

    fun inputOperator(operation: Operations) {
        val symbol = operation.symbol
        val baseExpression = if (state.isResultShown) state.result else state.expression

        if (baseExpression.isEmpty()) {
            if (operation == Operations.SUBTRACT) {
                state = state.copy(
                    expression = Operations.SUBTRACT.symbol,
                    result = Operations.SUBTRACT.symbol,
                    errorMessage = null,
                    isResultShown = false
                )
            }
            return
        }

        val lastChar = baseExpression.last().toString()

        if (isOperator(lastChar) || lastChar == ".") {
            return
        }

        val newExpression = baseExpression + symbol
        state = state.copy(
            expression = newExpression,
            result = newExpression,
            errorMessage = null,
            isResultShown = false
        )
    }


    fun clear() {
        state = CalculatorStates()
    }

    fun evaluate() {
        if (state.expression.isEmpty()) {
            return
        }

        try {
            val value = evaluator.evaluate(state.expression)
            val formattedValue = formatResult(value)

            state = state.copy(
                result = formattedValue,
                errorMessage = null,
                isResultShown = true
            )
        } catch (e: ArithmeticException) {
            state = state.copy(
                result = context.getString(R.string.error_title),
                errorMessage = context.getString(R.string.error_division_by_zero),
                isResultShown = true
            )
        } catch (e: Exception) {
            state = state.copy(
                result = context.getString(R.string.error_title),
                errorMessage = context.getString(R.string.error_invalid_expression),
                isResultShown = true
            )
        }
    }

    private fun getCurrentNumber(expression: String): String {
        var result = ""

        for (i in expression.length - 1 downTo 0) {
            val ch = expression[i]

            if (ch.isDigit() || ch == '.') {
                result = ch + result
            } else {
                break
            }
        }

        return result
    }

    private fun isOperator(value: String): Boolean {
        return value == Operations.ADD.symbol
                || value == Operations.SUBTRACT.symbol
                || value == Operations.MULTIPLY.symbol
                || value == Operations.DIVIDE.symbol
    }

    private fun formatResult(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toLong().toString()
        } else {
            value.toString()
        }
    }
}