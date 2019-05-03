package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import com.google.gson.Gson
import net.spy.memcached.MemcachedClient
import org.springframework.stereotype.Service

@Service
class StorageService(private val memcachedClient: MemcachedClient, private val gson: Gson) {

	fun storeUser(user: User) {
		val key = "users:${user.id}"
		val json = gson.toJson(user)
		val result = memcachedClient.set(key, 0, json).get()
		if (!result) {
			throw RuntimeException("failed to store user: ${user.id}")
		}
	}

	fun findUser(id: Int) = memcachedClient
			.get("users:$id")?.let { it as String }
			.let { jsonString ->
				gson.fromJson(jsonString, User::class.java)
			} ?: null

	fun storeAvailableCards(userId: Int, cards: List<Card>) {
		val key = "users:$userId:available_cards"
		val list = CardsDto(cards)
		val json = gson.toJson(list)
		val result = memcachedClient.set(key, 0, json).get()
		if (!result) {
			throw RuntimeException("failed to store available cards for user: $userId")
		}
	}

	fun updateUserAvailableCards(userId: Int, cards: List<Card>) {
		val key = "users:$userId:available_cards"
		val dataJsonString = memcachedClient.get(key)?.let { it as String }
		val updatedCards = dataJsonString?.let {
			val list = gson.fromJson(it, CardsDto::class.java).cards.toMutableList()
			list.addAll(cards)
			return@let list
		} ?: cards
		storeAvailableCards(userId, updatedCards)
	}

	fun getUserAvailableCards(userId: Int): List<Card> {
		val key = "users:$userId:available_cards"
		return memcachedClient.get(key)?.let { it as String }
				.let {
					gson.fromJson(it, CardsDto::class.java).cards
				}
	}

	data class CardsDto(val cards: List<Card>)

}
