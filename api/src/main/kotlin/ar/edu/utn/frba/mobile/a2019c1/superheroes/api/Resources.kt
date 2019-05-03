package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

data class UserData(val nickname: String? = null)

data class CardsData(val userId: Int, val quantity: Int)

data class UserResponseResource(val id: Int)

data class CardsResponseResource(val cards: List<Card>)

fun User.asResource() = UserResponseResource(id)

fun List<Card>.asResource() = CardsResponseResource(this)
