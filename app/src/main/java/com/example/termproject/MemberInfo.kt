package com.example.termproject

data class MemberInfo(
    var Uid: String? = null,
    var Name: String? = null,
    var Phone: String? = null,
    var Birth: String? = null,
    var Email: String? = null,
    var Friends: List<String>? = null,
    val type: String = "user"
)