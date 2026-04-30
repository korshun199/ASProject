package com.oleg.hello

/*
 * ЕЛЕНА ИГОРЕВНА:
 * Слушай внимательно. В этой версии я добавила управление фокусом и расширенную логику.
 * Мы работаем в ветке feature/logic-step-two, и здесь всё должно быть безупречно.
 */
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloOlegTheme {
                // Запускаем обновленный экран второго этапа
                OlegStepTwoScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OlegStepTwoScreen() {
    // Состояния: ввод пользователя и результат валидации
    var userInput by remember { mutableStateOf("") }
    var accessStatus by remember { mutableStateOf<AccessLevel>(AccessLevel.NONE) }

    // Менеджер фокуса для управления клавиатурой
    val focusManager = LocalFocusManager.current

    // Анимация цвета фона в зависимости от статуса доступа
    val cardColor by animateColorAsState(
        targetValue = when (accessStatus) {
            AccessLevel.GRANTED -> MaterialTheme.colorScheme.primaryContainer
            AccessLevel.DENIED -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.surfaceVariant
        }, label = "CardColorAnimation"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "HELLO OLEG: LOGIC STEP",
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
                        // Скрываем клавиатуру при нажатии
                        focusManager.clearFocus()

                        // Логика проверки: только "Олежка" или "Oleg" получают статус GRANTED
                        accessStatus = if (userInput.trim().equals("Олежка", ignoreCase = true) ||
                            userInput.trim().equals("Oleg", ignoreCase = true)) {
                            AccessLevel.GRANTED
                        } else {
                            AccessLevel.DENIED
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("ПРОВЕРИТЬ СТАТУС", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Отображение результата
                if (accessStatus != AccessLevel.NONE) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = cardColor),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = if (accessStatus == AccessLevel.GRANTED) "СТАТУС: ПОДТВЕРЖДЕН" else "СТАТУС: ОТКЛОНЕН",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (accessStatus == AccessLevel.GRANTED)
                                    MaterialTheme.colorScheme.onPrimaryContainer else
                                    MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (accessStatus == AccessLevel.GRANTED)
                                    "Добро пожаловать в систему. Все протоколы активны." else
                                    "Ошибка идентификации. Попробуйте снова или обратитесь к администратору.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

// Перечисление для уровней доступа - так код становится чище и понятнее
enum class AccessLevel {
    NONE, GRANTED, DENIED
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StepTwoPreview() {
    HelloOlegTheme {
        OlegStepTwoScreen()
    }
}