package com.example.medicalcalculator.presentation.results

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.medicalcalculator.presentation.authentication.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditResultScreen(
    score: Int,
    resultViewModel: ResultViewModel = remember { ResultViewModel() },
    authViewModel: AuthViewModel = remember { AuthViewModel() }
) {
    val list by resultViewModel.list.collectAsState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val riskLevel = resultViewModel.getAuditRiskLevel(score)
    val (riskColor, riskIcon) = when {
        score <= 7 -> Color(0xFF4CAF50) to Icons.Filled.CheckCircle
        score <= 15 -> Color(0xFFFFA726) to Icons.Filled.Warning
        score <= 19 -> Color(0xFFFF7043) to Icons.Filled.Error
        else -> Color(0xFFE53935) to Icons.Filled.ErrorOutline
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5F5F5),
                        Color(0xFFE8F5E9)
                    )
                )
            )
    ) {
        IconButton(
            onClick = { authViewModel.signOut() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.9f))
        ) {
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = "Выйти",
                tint = Color(0xFF616161)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(top = 40.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = riskColor.copy(alpha = 0.3f)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = riskIcon,
                        contentDescription = null,
                        tint = riskColor,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Результат AUDIT",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.5.sp
                        ),
                        color = Color(0xFF424242)
                    )

                    Spacer(Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(riskColor.copy(alpha = 0.1f))
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = "$score",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 56.sp
                            ),
                            color = riskColor
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = riskLevel,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = riskColor,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                resultViewModel.saveAuditResult(score)
                                resultViewModel.getAuditResult()
                                showBottomSheet = true
                                scope.launch { sheetState.show() }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                width = 2.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.History,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "История",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.DateRange,
                    title = "Дата теста",
                    value = DateFormat.getDateInstance().format(Date()),
                    color = Color(0xFF1976D2)
                )

                InfoCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.TrendingUp,
                    title = "Всего тестов",
                    value = "${list.size}",
                    color = Color(0xFF7B1FA2)
                )
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = {
                Surface(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 48.dp, height = 4.dp)
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .navigationBarsPadding()
                    .padding(bottom = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "История результатов",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color(0xFF212121)
                    )

                    IconButton(onClick = { showBottomSheet = false }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Закрыть",
                            tint = Color(0xFF757575)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                if (list.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Inbox,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color(0xFFBDBDBD)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "История пока пуста",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF757575)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(items = list, key = { it.timestamp }) { result ->
                            val formatted = remember(result.timestamp) {
                                DateFormat.getDateTimeInstance().format(Date(result.timestamp))
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF5F5F5)
                                ),
                                elevation = cardElevation(0.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    when {
                                                        result.score <= 7 -> Color(0xFF4CAF50)
                                                        result.score <= 15 -> Color(0xFFFFA726)
                                                        result.score <= 19 -> Color(0xFFFF7043)
                                                        else -> Color(0xFFE53935)
                                                    }.copy(alpha = 0.2f)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${result.score}",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = when {
                                                    result.score <= 7 -> Color(0xFF4CAF50)
                                                    result.score <= 15 -> Color(0xFFFFA726)
                                                    result.score <= 19 -> Color(0xFFFF7043)
                                                    else -> Color(0xFFE53935)
                                                }
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = resultViewModel.getAuditRiskLevel(result.score),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Medium
                                                ),
                                                color = Color(0xFF424242)
                                            )
                                            Text(
                                                text = formatted,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color(0xFF757575)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    value: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        elevation = cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF616161)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = color
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuditResultScreenPreview() {
    AuditResultScreen(score = 12)
}