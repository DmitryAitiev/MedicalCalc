package com.example.medicalcalculator.presentation.results

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditResultScreen(
    score: Int,
    viewModel: ResultViewModel = remember { ResultViewModel() }
) {
    val list by viewModel.list.collectAsState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF4CAF50), Color(0xFFA5D6A7))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Card(
                shape = MaterialTheme.shapes.medium,
                elevation = cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ваш результат AUDIT",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF388E3C)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "$score баллов",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = viewModel.getAuditRiskLevel(score),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.saveAuditResult(score)
                            viewModel.getAuditResult()
                            showBottomSheet = true
                            scope.launch { sheetState.show() }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(48.dp)
                    ) {
                        Text("Предыдущие результаты")
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "История результатов",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF388E3C)
                )
                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(Modifier.height(8.dp))
                if (list.isEmpty()) {
                    Text(
                        text = "История пока пуста",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 16.dp),
                        color = Color.Gray
                    )
                }
                else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                    ) {
                        items(items = list, key = { it.timestamp }) { result ->
                            val formatted = remember(result.timestamp) {
                                DateFormat.getDateTimeInstance().format(Date(result.timestamp))
                            }
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${result.score} балл(ов)",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = formatted,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray
                                    )
                                }
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuditResultScreenPreview() {
    AuditResultScreen(score = 12)
}