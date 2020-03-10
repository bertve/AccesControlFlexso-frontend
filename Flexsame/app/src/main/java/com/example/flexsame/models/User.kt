package com.example.flexsame.models

data class User(val userId :Long
                ,var firstName :String
                ,var lastName :String
                ,var email: String
                ,var password: String
                ,var token : String) {

}