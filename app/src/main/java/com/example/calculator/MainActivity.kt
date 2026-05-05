package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.logics.Operations

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView

    private lateinit var engine: CalculatorEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        engine = CalculatorEngine(applicationContext)

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        val btn0: Button = findViewById(R.id.btn0)
        val btn1: Button = findViewById(R.id.btn1)
        val btn2: Button = findViewById(R.id.btn2)
        val btn3: Button = findViewById(R.id.btn3)
        val btn4: Button = findViewById(R.id.btn4)
        val btn5: Button = findViewById(R.id.btn5)
        val btn6: Button = findViewById(R.id.btn6)
        val btn7: Button = findViewById(R.id.btn7)
        val btn8: Button = findViewById(R.id.btn8)
        val btn9: Button = findViewById(R.id.btn9)

        val btnDot: Button = findViewById(R.id.btnDot)
        val btnPlus: Button = findViewById(R.id.btnPlus)
        val btnMinus: Button = findViewById(R.id.btnMinus)
        val btnMultiply: Button = findViewById(R.id.btnMultiply)
        val btnDivide: Button = findViewById(R.id.btnDivide)
        val btnEquals: Button = findViewById(R.id.btnEquals)
        val btnClear: Button = findViewById(R.id.btnClear)
        val btnBackspace: Button = findViewById(R.id.btnBackspace)

        setupDigitClickListener(btn0, "0")
        setupDigitClickListener(btn1, "1")
        setupDigitClickListener(btn2, "2")
        setupDigitClickListener(btn3, "3")
        setupDigitClickListener(btn4, "4")
        setupDigitClickListener(btn5, "5")
        setupDigitClickListener(btn6, "6")
        setupDigitClickListener(btn7, "7")
        setupDigitClickListener(btn8, "8")
        setupDigitClickListener(btn9, "9")

        btnDot.setOnClickListener {
            engine.inputDot()
            updateUi()
        }

        btnPlus.setOnClickListener {
            engine.inputOperator(Operations.ADD)
            updateUi()
        }

        btnMinus.setOnClickListener {
            engine.inputOperator(Operations.SUBTRACT)
            updateUi()
        }

        btnMultiply.setOnClickListener {
            engine.inputOperator(Operations.MULTIPLY)
            updateUi()
        }

        btnDivide.setOnClickListener {
            engine.inputOperator(Operations.DIVIDE)
            updateUi()
        }

        btnClear.setOnClickListener {
            engine.clear()
            updateUi()
        }

        btnBackspace.setOnClickListener {
            finish();
        }

        btnEquals.setOnClickListener {
            engine.evaluate()
            updateUi()
        }

        updateUi()
    }

    private fun setupDigitClickListener(button: Button, digit: String) {
        button.setOnClickListener {
            engine.inputDigit(digit)
            updateUi()
        }
    }

    private fun updateUi() {
        val state = engine.getState()

        tvExpression.text = state.expression

        if (state.errorMessage != null) {
            tvResult.text = state.errorMessage
        } else {
            tvResult.text = state.result
        }
    }
}