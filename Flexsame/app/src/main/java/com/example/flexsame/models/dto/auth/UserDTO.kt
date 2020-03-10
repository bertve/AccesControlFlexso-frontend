package com.example.flexsame.models.dto.auth


class UserDTO(val id:Long,
              val firstname : String,
              val lastName : String,
              val email : String,
              val password: String,
              val password_confirmation :String) {

}