package com.hsc.qda.data.model.quupa.position

data class Tag(
    val tagId: String,
    val tagName: String? = null,
    val lastPacketTS: Long,
    val color: String,
    val tagGroupName: String? = null,
    val locationType: String,
    val locationMovementStatus: String,
    val locationRadius: Double,
    val location: List<Double>,
    val locationTS: Long,
    val locationCoordSysId: String,
    val locationCoordSysName: String,
    val locationZoneIds: List<String>,
    val locationZoneNames: List<String>,
    val button1State: String,
    val button1StateTS: Long,
    val button1LastPressTS: Long,
    val batteryAlarm: String,
    val batteryAlarmTS: Long,
    val rssi: Int,
    val rssiLocatorCount: Int
)