package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

data class UserData(val nickname: String? = null)

data class UserResponseResource(val id: Int)

fun User.asResource() = UserResponseResource(id)
