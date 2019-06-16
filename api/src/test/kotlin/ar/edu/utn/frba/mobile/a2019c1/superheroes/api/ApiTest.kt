package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import ar.edu.utn.frba.mobile.a2019c1.superheroes.api.FightData.Geolocation
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [UsersController::class])
class UsersControllerTest(@Autowired val mockMvc: MockMvc) {

	@MockBean
	private lateinit var usersService: UsersService

	@Test
	fun testCreateUserSuccessfulResponse() {
		val body = JSONObject().put("nickname", "vegeta")
		val id = 123456
		val userData = UserData("vegeta")
		val user = User(id, "vegeta")
		whenever(usersService.createWith(userData)).thenReturn(user)
		mockMvc.perform(post("/superheroes/users")
				.content(body.toString())
				.contentType(APPLICATION_JSON))
				.andExpect(status().isCreated)
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("\$.id").value(id))
		verify(usersService).createWith(userData)
	}

	@Test
	fun testMissingNickname() {
		val body = JSONObject().put("nickname", null)
		val userData = UserData(null)
		whenever(usersService.createWith(userData)).thenThrow(IllegalArgumentException("missing nickname"))
		mockMvc.perform(post("/superheroes/users")
				.content(body.toString())
				.contentType(APPLICATION_JSON))
				.andExpect(status().isBadRequest)
				.andExpect(jsonPath("\$.message").value("missing nickname"))
		verify(usersService).createWith(userData)
	}

	@Test
	fun testGetUserAvailableCards() {
		val id = 123456
		val cards = listOf(
				Card(1, "hulk", "big", "hulk.jpg", 23),
				Card(2, "iron man", "metal", "iron_man.jpg", 45))
		whenever(usersService.getCardsOf(id)).thenReturn(cards)
		mockMvc.perform(get("/superheroes/users/123456/available_cards")
				.contentType(APPLICATION_JSON))
				.andExpect(status().isOk)
				.andExpect(jsonPath("\$.cards[0].id").value(1))
				.andExpect(jsonPath("\$.cards[0].name").value("hulk"))
				.andExpect(jsonPath("\$.cards[0].description").value("big"))
				.andExpect(jsonPath("\$.cards[0].thumbnail").value("hulk.jpg"))
				.andExpect(jsonPath("\$.cards[0].power").value(23))
				.andExpect(jsonPath("\$.cards[1].id").value(2))
				.andExpect(jsonPath("\$.cards[1].name").value("iron man"))
				.andExpect(jsonPath("\$.cards[1].description").value("metal"))
				.andExpect(jsonPath("\$.cards[1].thumbnail").value("iron_man.jpg"))
				.andExpect(jsonPath("\$.cards[1].power").value(45))
		verify(usersService).getCardsOf(id)
	}

	@Test
	fun testCreateTeam() {
		val body = JSONObject().put("superheroes", JSONArray()
				.put(1111)
				.put(2222)
				.put(3333)
				.put(4444))
		val superheroes = listOf(1111, 2222, 3333, 4444)
		val teamData = UserTeamData(123456, superheroes)
		val team = Team(45, superheroes)
		whenever(usersService.createTeam(teamData)).thenReturn(team)
		mockMvc.perform(post("/superheroes/users/123456/team")
				.content(body.toString())
				.contentType(APPLICATION_JSON))
				.andExpect(status().isCreated)
				.andExpect(jsonPath("\$.id").value(45))
		verify(usersService).createTeam(teamData)
	}

	@Test
	fun testFailedToCreateTeamBecauseEmptySuperheroes() {
		val body = JSONObject()
		mockMvc.perform(post("/superheroes/users/123456/team")
				.content(body.toString())
				.contentType(APPLICATION_JSON))
				.andExpect(status().isBadRequest)
				.andExpect(jsonPath("\$.message").value("missing superheroes"))
	}

	@Test
	fun testFailedToCreateTeamBecauseNotMeetSuperheroesQuantity() {
		val body = JSONObject().put("superheroes", JSONArray()
				.put(1111)
				.put(2222)
				.put(3333))
		mockMvc.perform(post("/superheroes/users/123456/team")
				.content(body.toString())
				.contentType(APPLICATION_JSON))
				.andExpect(status().isBadRequest)
				.andExpect(jsonPath("\$.message").value("a team requires 4 superheroes"))
	}

}

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [CardsController::class])
class CardsControllerTest(@Autowired val mockMvc: MockMvc) {

	@MockBean
	private lateinit var cardsService: CardsService

	@Test
	fun testOpenBundle() {
		val id = 123456
		val cards = listOf(
				Card(1, "hulk", "big", "hulk.jpg", 23),
				Card(2, "iron man", "metal", "iron_man.jpg", 45),
				Card(3, "captain america", "shield", "captain_america.jpg", 80))
		val userData = CardsData(id, quantity = 3)
		whenever(cardsService.getBundle(userData)).thenReturn(cards)
		mockMvc.perform(get("/superheroes/cards?user_id=123456&quantity=3")
				.contentType(APPLICATION_JSON))
				.andExpect(status().isOk)
				.andExpect(jsonPath("\$.cards[0].id").value(1))
				.andExpect(jsonPath("\$.cards[0].name").value("hulk"))
				.andExpect(jsonPath("\$.cards[0].description").value("big"))
				.andExpect(jsonPath("\$.cards[0].thumbnail").value("hulk.jpg"))
				.andExpect(jsonPath("\$.cards[0].power").value(23))
				.andExpect(jsonPath("\$.cards[1].id").value(2))
				.andExpect(jsonPath("\$.cards[1].name").value("iron man"))
				.andExpect(jsonPath("\$.cards[1].description").value("metal"))
				.andExpect(jsonPath("\$.cards[1].thumbnail").value("iron_man.jpg"))
				.andExpect(jsonPath("\$.cards[1].power").value(45))
		verify(cardsService).getBundle(userData)
	}

	@Test
	fun testMissingParameters() {
		mockMvc.perform(get("/superheroes/cards?quantity=3")
				.contentType(APPLICATION_JSON))
				.andExpect(status().isBadRequest)
				.andExpect(jsonPath("\$.message").value("missing user_id parameter"))
		mockMvc.perform(get("/superheroes/cards?user_id=&quantity=3")
				.contentType(APPLICATION_JSON))
				.andExpect(status().isBadRequest)
				.andExpect(jsonPath("\$.message").value("missing user_id parameter"))
		mockMvc.perform(get("/superheroes/cards?user_id=123456")
				.contentType(APPLICATION_JSON))
				.andExpect(status().isBadRequest)
				.andExpect(jsonPath("\$.message").value("missing quantity parameter"))
		mockMvc.perform(get("/superheroes/cards?user_id=123456&quantity=")
				.contentType(APPLICATION_JSON))
				.andExpect(status().isBadRequest)
				.andExpect(jsonPath("\$.message").value("missing quantity parameter"))
		mockMvc.perform(get("/superheroes/cards?user_id=123456&quantity=0")
				.contentType(APPLICATION_JSON))
				.andExpect(status().isBadRequest)
				.andExpect(jsonPath("\$.message").value("provide a value greater than 0 for quantity parameter"))
	}
}

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [TeamsController::class])
class TeamsControllerTest(@Autowired val mockMvc: MockMvc) {

	@MockBean
	private lateinit var teamsService: TeamsService

	@Test
	fun testGetTeam() {
		val id = 45
		val cards = listOf(
				Card(1, "hulk", "big", "hulk.jpg", 23),
				Card(2, "iron man", "metal", "iron_man.jpg", 45))
		whenever(teamsService.getTeam(id)).thenReturn(cards)
		mockMvc.perform(get("/superheroes/teams/45")
				.contentType(APPLICATION_JSON))
				.andExpect(status().isOk)
				.andExpect(jsonPath("\$.superheroes[0].id").value(1))
				.andExpect(jsonPath("\$.superheroes[0].name").value("hulk"))
				.andExpect(jsonPath("\$.superheroes[0].description").value("big"))
				.andExpect(jsonPath("\$.superheroes[0].thumbnail").value("hulk.jpg"))
				.andExpect(jsonPath("\$.superheroes[0].power").value(23))
				.andExpect(jsonPath("\$.superheroes[1].id").value(2))
				.andExpect(jsonPath("\$.superheroes[1].name").value("iron man"))
				.andExpect(jsonPath("\$.superheroes[1].description").value("metal"))
				.andExpect(jsonPath("\$.superheroes[1].thumbnail").value("iron_man.jpg"))
				.andExpect(jsonPath("\$.superheroes[1].power").value(45))
				.andExpect(jsonPath("\$.totalPower").value(68)) // FIXME
		verify(teamsService).getTeam(id)
	}

	@Test
	fun testTeamNotFound() {
		whenever(teamsService.getTeam(45)).thenThrow(TeamNotFoundException("team 45 not found"))
		mockMvc.perform(get("/superheroes/teams/45")
				.contentType(APPLICATION_JSON))
				.andExpect(status().isNotFound)
				.andExpect(jsonPath("\$.message").value("team 45 not found"))
		verify(teamsService).getTeam(45)
	}

}

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [FightController::class])
class FightControllerTest(@Autowired val mockMvc: MockMvc) {

	@MockBean
	private lateinit var fightService: FightService

	@Test
	fun testFight() {
		val id = 123
		val geolocation = Geolocation(amplitude = 222222, latitude = 333333)
		val timestamp = 15000000123
		val fightData = FightData(id, geolocation, timestamp)
		val body = JSONObject()
				.put("user_id", id)
				.put("geolocation", JSONObject()
						.put("amplitude", geolocation.amplitude)
						.put("latitude", geolocation.latitude))
				.put("timestamp", timestamp)
		val fightResult = FightResult(
				id = 123,
				winner = "goku",
				players = listOf(
						FightResult.Player(1, "goku", 301, 500),
						FightResult.Player(2, "vegeta", 405, 498)))
		whenever(fightService.fight(fightData)).thenReturn(fightResult)
		mockMvc.perform(post("/superheroes/fight")
				.content(body.toString())
				.contentType(APPLICATION_JSON))
				.andExpect(status().isOk)
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("\$.id").value(id))
				.andExpect(jsonPath("\$.winner").value("goku"))
				.andExpect(jsonPath("\$.opponent.id").value(2))
				.andExpect(jsonPath("\$.opponent.nickname").value("vegeta"))
				.andExpect(jsonPath("\$.opponent.team_id").value(405))
		verify(fightService).fight(fightData)
	}

}
