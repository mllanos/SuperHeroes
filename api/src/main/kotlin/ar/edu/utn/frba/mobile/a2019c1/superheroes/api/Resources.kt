package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import ar.edu.utn.frba.mobile.a2019c1.superheroes.api.FightResponseResource.Opponent

private const val MAX_CARDS_LIMIT = 8

private const val TEAM_SIZE = 4

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

data class UserTeamData(val userId: Int, val superheroes: List<Int>)

data class FightData(val user_id: Int, val geolocation: Geolocation, val timestamp: Long) {
	data class Geolocation(val latitude: Int, val amplitude: Int)
}

data class UserTeamResource(val superheroes: List<Int>? = null)

data class FightResponseResource(val id: Int, val winner: String, val opponent: Opponent) {
	data class Opponent(val id: Int, val nickname: String, val team_id: Int)
}

data class UserResponseResource(val id: Int)

data class CardsResponseResource(val cards: List<Card>)

data class UserTeamResourceResponse(val id: Int)

data class TeamResponseResource(val superheroes: List<Card>, val total_power: Int)

fun UserTeamResource.forUser(userId: Int): UserTeamData {
	requireNotNull(superheroes) { "missing superheroes" }
	if (superheroes.size != TEAM_SIZE) {
		throw IllegalArgumentException("a team requires 4 superheroes")
	}
	return UserTeamData(userId, superheroes)
}

fun User.asResource() = UserResponseResource(id)

fun List<Card>.asResource() = CardsResponseResource(cards = this)

fun Team.asResource() = UserTeamResourceResponse(id)

fun List<Card>.asCardsResource() = TeamResponseResource(superheroes = this, total_power = this.sumBy { it.power })

fun FightResult.asResource(): FightResponseResource {
	val player = players.first { p -> p.nickname != winner }
	val opponent = Opponent(player.id, player.nickname, player.teamId)
	return FightResponseResource(id, winner, opponent)
}
