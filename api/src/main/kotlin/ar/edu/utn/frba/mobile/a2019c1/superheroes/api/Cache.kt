package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import com.google.gson.Gson
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import net.spy.memcached.MemcachedClient
import org.springframework.stereotype.Service
import java.text.MessageFormat.format

private const val USER_KEY = "users:{0}"
private const val USER_CARDS_KEY = "users:{0}:available_cards"
private const val USER_TEAM_KEY = "users:{0}:team"
private const val TEAM_KEY = "users:{0}:team"
private const val FIGHT_AVAILABLE_PLAYERS_KEY = "fights:users"
private const val FIGHTS_KEY = "fights"
private const val FIGHT_TIMEOUT = 10000L

@Service
class StorageService(private val memcachedClient: MemcachedClient, private val gson: Gson) {

	fun storeUser(user: User) {
		val key = format(USER_KEY, user.id.toString())
		val json = gson.toJson(user)
		val result = memcachedClient.set(key, 0, json).get()
		if (!result) {
			throw RuntimeException("failed to store user: ${user.id}")
		}
	}

	fun findUser(id: Int): User? {
		val key = format(USER_KEY, id.toString())
		return memcachedClient.get(key)?.let { it as String }
				?.let { jsonString ->
					gson.fromJson(jsonString, User::class.java)
				}
	}

	fun storeAvailableCards(userId: Int, cards: List<Card>) {
		val key = format(USER_CARDS_KEY, userId.toString())
		val list = CardsDto(cards)
		val json = gson.toJson(list)
		val result = memcachedClient.set(key, 0, json).get()
		if (!result) {
			throw RuntimeException("failed to store available cards for user: $userId")
		}
	}

	fun updateUserAvailableCards(userId: Int, cards: List<Card>) {
		val key = format(USER_CARDS_KEY, userId.toString())
		val dataJsonString = memcachedClient.get(key)?.let { it as String }
		val updatedCards = dataJsonString?.let {
			val list = gson.fromJson(it, CardsDto::class.java).cards.toMutableList()
			list.addAll(cards)
			return@let list
		} ?: cards
		storeAvailableCards(userId, updatedCards)
	}

	fun getUserAvailableCards(userId: Int): List<Card> {
		val key = format(USER_CARDS_KEY, userId.toString())
		return memcachedClient.get(key)?.let { it as String }
				?.let {
					gson.fromJson(it, CardsDto::class.java).cards
				} ?: emptyList()
	}

	fun storeUserTeam(userId: Int, team: Team) {
		val userTeamKey = format(USER_TEAM_KEY, userId.toString())
		val teamKey = format(TEAM_KEY, userId.toString())
		val json = gson.toJson(team)
		val userTeamResult = memcachedClient.set(userTeamKey, 0, json).get()
		val teamResult = memcachedClient.set(teamKey, 0, json).get()
		if (!userTeamResult || !teamResult) {
			throw RuntimeException("failed to store team for user: $userId")
		}
	}

	fun findUserTeam(userId: Int): Team? {
		val userTeamKey = format(USER_TEAM_KEY, userId.toString())
		return memcachedClient.get(userTeamKey)?.let { it as String }
				?.let {
					gson.fromJson(it, Team::class.java)
				}
	}

	fun findTeam(teamId: Int): Team? {
		val key = format(TEAM_KEY, teamId.toString())
		return memcachedClient.get(key)?.let { it as String }
				?.let {
					gson.fromJson(it, Team::class.java)
				}
	}

	fun findAnOpponentFor(userId: Int): Int? {
		memcachedClient.get(FIGHT_AVAILABLE_PLAYERS_KEY)?.let { it as String }
				?.let {
					val opponentsDto = gson.fromJson(it, OpponentsDto::class.java)
					addOpponent(userId, opponentsDto.opponents)
				} ?: saveOpponent(userId)
		var opponent: Int? = null
		runBlocking {
			withTimeout(FIGHT_TIMEOUT) {
				while (opponent == null && isActive) {
					memcachedClient.get(FIGHT_AVAILABLE_PLAYERS_KEY).let { it as String }
							.let {
								val opponentsDto = gson.fromJson(it, OpponentsDto::class.java)
								opponent = opponentsDto.opponents
										.stream()
										.filter { opponentId -> opponentId != userId }
										.findFirst()
										.map { found -> found }
										.orElse(null)
							}
				}
			}
		}
		return opponent
	}

	fun storeFight(fightResult: FightResult) {
		memcachedClient.get(FIGHTS_KEY)?.let { it as String }
				?.let { jsonString ->
					val fights = gson.fromJson(jsonString, FightDto::class.java).fights.toMutableList()
					updateFights(fights, fightResult)
				} ?: addNewFight(fightResult)
	}

	private fun updateFights(fights: MutableList<FightResult>, fightResult: FightResult) {
		fights.add(fightResult)
		val fightDto = FightDto(fights)
		val result = memcachedClient.set(FIGHTS_KEY, 0, gson.toJson(fightDto)).get()
		if (!result) {
			throw RuntimeException("failed to store fight ${fightResult.id}")
		}
	}

	private fun addNewFight(fightResult: FightResult) {
		val fightDto = FightDto(listOf(fightResult))
		val json = gson.toJson(fightDto)
		val result = memcachedClient.set(FIGHTS_KEY, 0, json).get()
		if (!result) {
			throw RuntimeException("failed to store fight ${fightResult.id}")
		}
	}

	private fun saveOpponent(userId: Int) {
		val opponentsDto = OpponentsDto(listOf(userId))
		val json = gson.toJson(opponentsDto)
		val result = memcachedClient.set(FIGHT_AVAILABLE_PLAYERS_KEY, 0, json).get()
		if (!result) {
			throw RuntimeException("failed to store opponent: $userId")
		}
	}

	private fun addOpponent(userId: Int, opponents: List<Int>) {
		val newOpponents = opponents.toMutableList()
		newOpponents.add(userId)
		val fightDto = OpponentsDto(newOpponents)
		val json = gson.toJson(fightDto)
		val result = memcachedClient.set(FIGHT_AVAILABLE_PLAYERS_KEY, 0, json).get()
		if (!result) {
			throw RuntimeException("failed to store opponent: $userId")
		}
	}

	data class FightDto(val fights: List<FightResult>)

	data class OpponentsDto(val opponents: List<Int>)

	data class CardsDto(val cards: List<Card>)

}
