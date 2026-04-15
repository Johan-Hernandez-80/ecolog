package com.example.ecolog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ecolog.ui.theme.EcoLogTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen(
    logsFlow: StateFlow<List<CarbonLog>>,
    onAddClick: () -> Unit,
    onDeleteLog: (String) -> Unit
) {
    val logs by logsFlow.collectAsState()

    val totalEmission = logs.sumOf { it.carbonEmission }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->

        Column(modifier = Modifier.padding(padding)) {

            // Header
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Carbon Track", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(12.dp))

                Card {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Total Emissions")
                        Text(
                            "${"%.1f".format(totalEmission)} kg CO₂",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }

            // List
            if (logs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No activities logged yet")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(logs, key = { it.id }) { log ->
                        LogItem(log, onDeleteLog)
                    }
                }
            }
        }
    }
}

@Composable
fun LogItem(
    log: CarbonLog,
    onDelete: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(log.activityName, fontWeight = FontWeight.Bold)
                Text(log.category)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text("${"%.1f".format(log.carbonEmission)}")
                    Text("kg CO₂", style = MaterialTheme.typography.bodySmall)
                }

                IconButton(onClick = { onDelete(log.id) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val sampleLogs = listOf(
        CarbonLog("1", "Commute", "Transport", 2.5),
        CarbonLog("2", "Beef Burger", "Food", 5.0),
        CarbonLog("3", "Electricity", "Home", 1.2)
    )
    val logsFlow = MutableStateFlow(sampleLogs)
    EcoLogTheme {
        HomeScreen(
            logsFlow = logsFlow,
            onAddClick = {},
            onDeleteLog = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenEmptyPreview() {
    val logsFlow = MutableStateFlow(emptyList<CarbonLog>())
    EcoLogTheme {
        HomeScreen(
            logsFlow = logsFlow,
            onAddClick = {},
            onDeleteLog = {}
        )
    }
}