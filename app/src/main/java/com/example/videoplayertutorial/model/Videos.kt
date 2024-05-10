package com.example.videoplayertutorial.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import java.sql.Timestamp
import java.util.Date

@Serializable
data class Videos(
    val id:Int,
    val created_at:JsonElement,
    val name:String,
    val channel_name:String,
    val thumbnail:String,
    val likes:Int,
)
