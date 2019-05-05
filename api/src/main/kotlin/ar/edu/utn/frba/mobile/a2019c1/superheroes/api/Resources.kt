package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

private const val MAX_CARDS_LIMIT = 8

data class UserData(val nickname: String? = null)

data class CardsData(val userId: Int? = null, var quantity: Int? = null) {
	init {
		requireNotNull(userId) { "missing user_id parameter" }
		requireNotNull(quantity) { "missing quantity parameter" }
		if (quantity!! < 1) {
			throw IllegalArgumentException("provide a value greater than 0 for quantity parameter")
		}
		quantity = minOf(quantity!!, MAX_CARDS_LIMIT)
	}
}

data class UserResponseResource(val id: Int)

data class CardsResponseResource(val cards: List<Card>)

fun User.asResource() = UserResponseResource(id)

fun List<Card>.asResource() = CardsResponseResource(this)
