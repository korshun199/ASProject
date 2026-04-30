package com.oleg.hello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oleg.hello.ui.theme.HelloOlegTheme

/*
 * Елена Игоревна: MainActivity — это база.
 * Мы работаем в ветке feature/logic-step-two.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloOlegTheme {
                OlegStepTwoScreen()
            }
        }
    }
}

// Уровни доступа для чистоты архитектуры
enum class AccessLevel {
    NONE, GRANTED, DENIED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OlegStepTwoScreen() {
    // Состояния интерфейса
    var userInput by remember { mutableStateOf("") }
    var accessStatus by remember { mutableStateOf(AccessLevel.NONE) }

    // Инструмент для управления клавиатурой
    val focusManager = LocalFocusManager.current

    // Анимация цвета в зависимости от статуса доступа
    val cardColor by animateColorAsState(
        targetValue = when (accessStatus) {
            AccessLevel.GRANTED -> MaterialTheme.colorScheme.primaryContainer
            AccessLevel.DENIED -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.surfaceVariant
        }, label = "ColorAnimation"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "HELLO OLEG: STEP 2",
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Валидация протокола",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = userInput,
                    onValueChange = {
                        userInput = it
                        accessStatus = AccessLevel.NONE
                    },
                    label = { Text("Идентификатор доступа") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus() // Скрываем клавиатуру

                        // Логика проверки: только "Олежка" или "Oleg"
                        accessStatus = if (userInput.trim().equals("Олежка", ignoreCase = true) ||
                            userInput.trim().equals("Oleg", ignoreCase = true)) {
                            AccessLevel.GRANTED
                        } else {
                            AccessLevel.DENIED
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("ПРОВЕРИТЬ СТАТУС", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Отображение результата валидации
                if (accessStatus != AccessLevel.NONE) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = if (accessStatus == AccessLevel.GRANTED) "ДОСТУП РАЗРЕШЕН" else "ОТКАЗАНО",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (accessStatus == AccessLevel.GRANTED)
                                    MaterialTheme.colorScheme.onPrimaryContainer else
                                    MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (accessStatus == AccessLevel.GRANTED)
                                    "Добро пожаловать. Система синхронизирована." else
                                    "Неверный идентификатор. Повторите ввод."
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StepTwoPreview() {
    HelloOlegTheme {
        OlegStepTwoScreen()
    }
}