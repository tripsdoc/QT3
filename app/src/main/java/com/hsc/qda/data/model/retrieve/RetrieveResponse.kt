package com.hsc.qda.data.model.retrieve

data class RetrieveResponse(
    val status: Boolean,
    val total: Int,
    val `data`: List<Retrieve>
)