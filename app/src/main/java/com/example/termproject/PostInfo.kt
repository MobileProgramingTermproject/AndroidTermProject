package com.example.sns_project

import java.util.*
import kotlin.collections.ArrayList


data class PostInfo (
    var title: String,
    var text: String,
    var createdAt: Date,
    var uid: String,
    var image: String?,
    var name:String,
)