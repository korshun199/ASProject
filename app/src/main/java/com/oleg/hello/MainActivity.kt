package com.oleg.hello

/*
 * Елена Игоревна: Я добавила финальные штрихи.
 * Чистота кода — это чистота твоих помыслов. Учись.
 */
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oleg.hello.ui.theme.HelloOlegTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloOlegTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OlegFinalDesignScreen()
                }
            }
        }
    }
}

@Composable
fun OlegFinalDesignScreen() {
    // ПАМЯТЬ СОСТОЯНИЯ
    var nameInput by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    // Ошибка ввода (для валидации)
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Проект Designe",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = nameInput,
            onValueChange = {
                nameInput = it
                isError = false // Сбрасываем ошибку при вводе
            },
            label = { Text("Имя пользователя") },
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            supportingText = {
                if (isError) Text("Имя не может быть пустым")
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (nameInput.isNotBlank()) {
                    resultText = "Привет, $nameInput! Изменения зафиксированы."
                    isError = false
                } else {
                    isError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("КОММИТИТЬ ИЗМЕНЕНИЯ", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(48.dp))

        if (resultText.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = resultText,
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OlegFinalPreview() {
    HelloOlegTheme {
        OlegFinalDesignScreen()
    }
}