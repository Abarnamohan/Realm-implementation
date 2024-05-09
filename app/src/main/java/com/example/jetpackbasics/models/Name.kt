package com.example.jetpackbasics.models

data class Name(

    val firstName : String,

    val lastName : String

){

     fun fullName(): String {
        return "$firstName $lastName"

    }
}
