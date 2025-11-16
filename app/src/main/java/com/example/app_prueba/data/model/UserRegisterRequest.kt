package com.example.app_prueba.data.model

import com.google.gson.annotations.SerializedName

data class UserRegisterRequest(
    val name: String?, // <-- CORREGIDO: Hecho nullable (String?)
    val email: String,
    @SerializedName("pass")
    val pass: String,
    @SerializedName("has_duoc_discount")
    val hasDuocDiscount: Boolean
)