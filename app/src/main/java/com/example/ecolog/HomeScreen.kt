package com.example.ecolog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    logs: List<CarbonLog>,
    onAddClick: () -> Unit,
    onDeleteLog: (String) -> Unit
) {
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
                Text("EcoLog", style = MaterialTheme.typography.headlineMedium)

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
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = when (log.category.lowercase()) {
                "transportation" -> Icons.Default.DirectionsCar
                "food" -> Icons.Default.Restaurant
                "home" -> Icons.Default.Home
                "waste" -> Icons.Default.Delete
                else -> Icons.Default.Help
            }

            Icon(
                imageVector = icon,
                contentDescription = log.category,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

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
