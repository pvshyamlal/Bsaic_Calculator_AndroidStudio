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
                CalculatorButton("C", buttonModifier) { display = "0" }
                CalculatorButton("%", buttonModifier) { display = appendToDisplay(display, "%") }
                CalculatorButton("/", buttonModifier) { display = appendToDisplay(display, "/") }
            }

            // Second Row: 7, 8, 9, *
            Row(modifier = Modifier.weight(1f)) {
                CalculatorButton("7", buttonModifier) { display = appendToDisplay(display, "7") }
                CalculatorButton("8", buttonModifier) { display = appendToDisplay(display, "8") }
                CalculatorButton("9", buttonModifier) { display = appendToDisplay(display, "9") }
                CalculatorButton("*", buttonModifier) { display = appendToDisplay(display, "*") }
            }

            // Third Row: 4, 5, 6, -
            Row(modifier = Modifier.weight(1f)) {
                CalculatorButton("4", buttonModifier) { display = appendToDisplay(display, "4") }
                CalculatorButton("5", buttonModifier) { display = appendToDisplay(display, "5") }
                CalculatorButton("6", buttonModifier) { display = appendToDisplay(display, "6") }
                CalculatorButton("-", buttonModifier) { display = appendToDisplay(display, "-") }
            }

            // Fourth Row: 1, 2, 3, +
            Row(modifier = Modifier.weight(1f)) {
                CalculatorButton("1", buttonModifier) { display = appendToDisplay(display, "1") }
                CalculatorButton("2", buttonModifier) { display = appendToDisplay(display, "2") }
                CalculatorButton("3", buttonModifier) { display = appendToDisplay(display, "3") }
                CalculatorButton("+", buttonModifier) { display = appendToDisplay(display, "+") }
            }

            // Fifth Row: 0, =, .
            Row(modifier = Modifier.weight(1f)) {
                CalculatorButton("0", buttonModifier) { display = appendToDisplay(display, "0") }
                CalculatorButton(".", buttonModifier) { display = appendToDisplay(display, ".") }
                CalculatorButton("=", buttonModifier) { display = calculateResult(display) }
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

fun appendToDisplay(currentDisplay: String, value: String): String {
    return if (currentDisplay == "0") value else currentDisplay + value
}

fun calculateResult(display: String): String {
    return try {
        val expression = display.replace("%", "/100")
        val result = eval(expression)
        result.toString()
    } catch (e: Exception) {
        "Error"
    }
}

// A simple function to evaluate the expression, consider using external libraries for complex calculations
fun eval(expression: String): Double {
    return expression.split("+", "-", "*", "/").map { it.toDouble() }.reduce { acc, next -> acc + next }
}
