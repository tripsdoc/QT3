package com.hsc.qda.data.model.quupa.qda

data class QdaPositionResponse(
    val responseTimestampEpoch: Long,
    val color: String,
    val positionTimestamp: String,
    val version: String,
    val positionTimestampEpoch: Long,
    val positionX: Double,
    val positionY: Double,
    val positionZ: Double,
    val areaId: String,
    val areaName: String,
    val id: String,
    val smoothedPositionZ: Double,
    val smoothedPositionY: Double,
    val smoothedPositionX: Double
)