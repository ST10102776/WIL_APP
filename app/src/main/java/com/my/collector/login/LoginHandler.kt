package com.my.collector.login

class LoginHandler {
    private val userPassword = "password"
    private val userName = "root"
    fun validateCredentials(username: String, password: String): Boolean {
        return username == userName && password == userPassword
    }
}