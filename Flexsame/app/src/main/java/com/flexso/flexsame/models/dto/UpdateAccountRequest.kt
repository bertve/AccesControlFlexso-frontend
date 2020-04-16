package com.flexso.flexsame.models.dto

data class UpdateAccountRequest(var userId: Long,
                                var firstName: String,
                                var lastName: String,
                                var email: String,
                                var password: String)