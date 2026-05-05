package com.example.calculator.logics

data class CalculatorStates(
    val expression: String = "",
    val result: String = "0",
    val errorMessage: String? = null,
    val isResultShown: Boolean = false
)
