package com.example.ecolog

import kotlinx.serialization.Serializable

@Serializable
data class CarbonLog(
    val id: String,
    val activityName: String,
    val category: String,
    val carbonEmission: Double
)