package com.example.calculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatorapp.ui.theme.CalculatorAppTheme
//import javax.script.ScriptEngineManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorAppTheme {
                CalculatorScreen()
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var display by remember { mutableStateOf("0") }
    val justCalculated = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display Area
        Text(
            text = display,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.LightGray)
                .padding(16.dp)
        )

        // Button Layout
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val buttonModifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(8.dp)

            // First Row: C, %, /
            Row(modifier = Modifier.weight(1f)) {
                CalculatorButton("C", buttonModifier) {
                    display = "0"
                    justCalculated.value = false
                }
                CalculatorButton("%", buttonModifier) { display = appendOperator(display, "%", justCalculated) }
                CalculatorButton("/", buttonModifier) { display = appendOperator(display, "/", justCalculated) }
            }

            // Second Row: 7, 8, 9, *
            Row(modifier = Modifier.weight(1f)) {
                CalculatorButton("7", buttonModifier) { display = appendNumber(display, "7", justCalculated) }
                CalculatorButton("8", buttonModifier) { display = appendNumber(display, "8", justCalculated) }
                CalculatorButton("9", buttonModifier) { display = appendNumber(display, "9", justCalculated) }
                CalculatorButton("*", buttonModifier) { display = appendOperator(display, "*", justCalculated) }
            }

            // Third Row: 4, 5, 6, -
            Row(modifier = Modifier.weight(1f)) {
                CalculatorButton("4", buttonModifier) { display = appendNumber(display, "4", justCalculated) }
                CalculatorButton("5", buttonModifier) { display = appendNumber(display, "5", justCalculated) }
                CalculatorButton("6", buttonModifier) { display = appendNumber(display, "6", justCalculated) }
                CalculatorButton("-", buttonModifier) { display = appendOperator(display, "-", justCalculated) }
            }

            // Fourth Row: 1, 2, 3, +
            Row(modifier = Modifier.weight(1f)) {
                CalculatorButton("1", buttonModifier) { display = appendNumber(display, "1", justCalculated) }
                CalculatorButton("2", buttonModifier) { display = appendNumber(display, "2", justCalculated) }
                CalculatorButton("3", buttonModifier) { display = appendNumber(display, "3", justCalculated) }
                CalculatorButton("+", buttonModifier) { display = appendOperator(display, "+", justCalculated) }
            }

            // Fifth Row: 0, =, .
            Row(modifier = Modifier.weight(1f)) {
                CalculatorButton("0", buttonModifier) { display = appendNumber(display, "0", justCalculated) }
                CalculatorButton(".", buttonModifier) { display = appendNumber(display, ".", justCalculated) }
                CalculatorButton("=", buttonModifier) {
                    display = calculateResult(display)
                    justCalculated.value = true
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
    ) {
        Text(text = text, fontSize = 24.sp, color = Color.White)
    }
}

// Append number to display, reset if result was just calculated
fun appendNumber(currentDisplay: String, value: String, justCalculated: MutableState<Boolean>): String {
    return if (justCalculated.value || currentDisplay == "0") {
        justCalculated.value = false // Reset the calculation flag
        value
    } else {
        currentDisplay + value
    }
}

// Append operator to display without resetting the current result
fun appendOperator(currentDisplay: String, operator: String, justCalculated: MutableState<Boolean>): String {
    return if (justCalculated.value) {
        justCalculated.value = false // Keep the current result and append the operator
        currentDisplay + operator
    } else {
        currentDisplay + operator
    }
}

// Calculate the result of the expression
fun calculateResult(display: String): String {
    return try {
        val sanitizedDisplay = display.replace("%", "/100") // Handle percentage
        val result = evaluateExpression(sanitizedDisplay)
        formatResult(result)
    } catch (e: Exception) {
        "Error"
    }
}

// Evaluate a simple mathematical expression
fun evaluateExpression(expression: String): Double {
    val tokens = expression.split("(?<=[-+*/])|(?=[-+*/])".toRegex()) // Split into numbers and operators
    val numbers = mutableListOf<Double>()
    val operators = mutableListOf<String>()

    tokens.forEach {
        when {
            it.toDoubleOrNull() != null -> numbers.add(it.toDouble())
            it in listOf("+", "-", "*", "/") -> operators.add(it)
        }
    }

    // Evaluate multiplication and division first
    var i = 0
    while (i < operators.size) {
        if (operators[i] == "*" || operators[i] == "/") {
            val num1 = numbers.removeAt(i)
            val num2 = numbers.removeAt(i)
            val result = if (operators[i] == "*") num1 * num2 else num1 / num2
            numbers.add(i, result)
            operators.removeAt(i)
        } else {
            i++
        }
    }

    // Evaluate addition and subtraction
    i = 0
    while (i < operators.size) {
        val num1 = numbers.removeAt(i)
        val num2 = numbers.removeAt(i)
        val result = if (operators[i] == "+") num1 + num2 else num1 - num2
        numbers.add(i, result)
        operators.removeAt(i)
    }

    return numbers.first()
}

// Function to format the result and show decimals only when necessary
fun formatResult(result: Double): String {
    return if (result == result.toInt().toDouble()) {
        result.toInt().toString() // Show as integer if the result is whole number
    } else {
        result.toString() // Show as float if it's a decimal number
    }
}
