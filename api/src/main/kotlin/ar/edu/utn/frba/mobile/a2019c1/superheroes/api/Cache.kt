package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import com.google.gson.Gson
import net.spy.memcached.MemcachedClient
import org.springframework.stereotype.Service
import java.text.MessageFormat.format

private const val USER_KEY = "users:{0}"
private const val USER_CARDS_KEY = "users:{0}:available_cards"
private const val USER_TEAM_KEY = "users:{0}:team"
private const val TEAM_KEY = "users:{0}:team"

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

	fun findTeam(teamId: Int): Team? {
		val key = format(TEAM_KEY, teamId.toString())
		return memcachedClient.get(key)?.let { it as String }
				?.let {
					gson.fromJson(it, Team::class.java)
				}
	}

	data class CardsDto(val cards: List<Card>)

}
