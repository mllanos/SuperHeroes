package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.spy.memcached.MemcachedClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.net.InetSocketAddress

@ExtendWith(MockitoExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StorageServiceTest {

	private val memcachedClient = MemcachedClient(InetSocketAddress("127.0.0.1", 11211))

	private lateinit var gson: Gson

	@BeforeAll
	fun setUp() {
		gson = GsonBuilder()
				.serializeNulls()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.create()!!
	}

	@AfterEach
	fun afterEach() {
		memcachedClient.flush()
	}

	@AfterAll
	fun afterAll() {
		memcachedClient.shutdown()
	}

	@Test
	fun testStoreUser() {
		val storageService = StorageService(memcachedClient, gson)
		val user = User(123456, "vegeta")
		storageService.storeUser(user)
		memcachedClient
				.get("users:123456")
				.let { it as String }
				.also { jsonString ->
					val userDto = gson.fromJson(jsonString, User::class.java)
					assertThat(userDto).isEqualToComparingFieldByField(user)
				}
	}

	@Test
	fun testFindCards() {
		val storageService = StorageService(memcachedClient, gson)
		val key = "users:123456:available_cards"
		val cards = listOf(
				Card(1, "hulk", "big", "hulk.jpg", 23),
				Card(2, "iron man", "metal", "iron_man.jpg", 45))
		val data = StorageService.CardsDto(cards)
		memcachedClient.set(key, 10, gson.toJson(data))
		storageService
				.getUserAvailableCards(123456)
				.also { found ->
					assertThat(found).hasSameElementsAs(cards)
				}
	}

	@Test
	fun testUpdateUserCards() {
		val storageService = StorageService(memcachedClient, gson)
		val key = "users:123456:available_cards"
		val storedCards = listOf(
				Card(1, "captain america", "shield", "captain_america.jpg", 77),
				Card(2, "spider man", "web", "spider_man.jpg", 64))
		val cards = listOf(
				Card(1, "hulk", "big", "hulk.jpg", 23),
				Card(2, "iron man", "metal", "iron_man.jpg", 45))
		val data = StorageService.CardsDto(storedCards)
		memcachedClient.set(key, 10, gson.toJson(data))
		storageService
				.updateUserAvailableCards(123456, cards)
				.also {
					storageService.getUserAvailableCards(123456).also {
						assertThat(it).containsAll(storedCards)
						assertThat(it).containsAll(cards)
					}
				}
	}

	@Test
	fun testStoreTeam() {
		val storageService = StorageService(memcachedClient, gson)
		val userId = 123456
		val team = Team(45, listOf(1, 2, 3, 4))
		storageService.storeUserTeam(userId, team)
		memcachedClient.get("users:123456:team").let { it as String }
				.let {
					val userDto = gson.fromJson(it, Team::class.java)
					assertThat(userDto).isEqualToComparingFieldByField(team)
				}
	}

	@Test
	fun testFindAnExistentOpponent() {
		val storageService = StorageService(memcachedClient, gson)
		val userId = 123456
		val opponentId = 100000
		val fightDto = StorageService.FightDto(listOf(opponentId))
		val json = gson.toJson(fightDto)
		memcachedClient.set("fights:users", 15, json).get()
		storageService
				.findAnOpponentFor(userId)
				.also { found ->
					assertThat(found).isEqualTo(opponentId)
				}
	}

	@Test
	fun testCannotFindAnOpponent() {
		val storageService = StorageService(memcachedClient, gson)
		val userId = 123456
		storageService.findAnOpponentFor(userId)
				.also { result ->
					assertThat(result).isNull()
				}
	}

}
