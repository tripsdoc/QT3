package com.hsc.qda.data.model.quupa.position

data class TagPositionResponse(
    val code: Int,
    val command: String,
    val message: String,
    val responseTS: Long,
    val status: String,
    val tags: List<Tag>,
    val version: String
)