package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import ar.edu.utn.frba.mobile.a2019c1.superheroes.api.FightData.Geolocation
import ar.edu.utn.frba.mobile.a2019c1.superheroes.api.FightResult.Player
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness

@ExtendWith(MockitoExtension::class)
class UsersServiceTest {

	@Mock
	private lateinit var randomService: RandomService

	@Mock
	private lateinit var storageService: StorageService

	@Test
	fun testCreateUser() {
		val usersService = UsersService(randomService, storageService)
		val userData = UserData("vegeta")
		whenever(randomService.generate(1000000)).doReturn(123456)
		usersService
				.createWith(userData)
				.also { user ->
					assertThat(user.nickname).isEqualTo("vegeta")
					assertThat(user.id).isEqualTo(123456)
					verify(randomService).generate(1000000)
					verify(storageService).storeUser(user)
				}
	}

	@Test
	fun testMissingNickname() {
		val usersService = UsersService(randomService, storageService)
		val userData = UserData(null)
		assertThrows<IllegalArgumentException> { usersService.createWith(userData) }
				.also { e ->
					assertThat(e.message).isEqualTo("missing nickname")
				}
	}

	@Test
	fun testGetUser() {
		val usersService = UsersService(randomService, storageService)
		val user = User(123456, "vegeta")
		whenever(storageService.findUser(123456)).doReturn(user)
		usersService
				.getUser(123456)
				.also { found ->
					assertThat(found).isEqualToComparingFieldByField(user)
					verify(storageService).findUser(123456)
				}
	}

	@Test
	fun testCannotGetUser() {
		val usersService = UsersService(randomService, storageService)
		assertThrows<UserNotFoundException> { usersService.getUser(123456) }
				.also { e ->
					assertThat(e.message).isEqualTo("user 123456 not found")
				}
	}

	@Test
	fun testGetUserAvailableCards() {
		val usersService = UsersService(randomService, storageService)
		val user = User(123456, "vegeta")
		val cards = listOf(
				Card(1, "hulk", "big", "hulk.jpg", 23),
				Card(2, "iron man", "metal", "iron_man.jpg", 45))
		whenever(storageService.findUser(123456)).doReturn(user)
		whenever(storageService.getUserAvailableCards(123456)).doReturn(cards)
		usersService
				.getCardsOf(123456)
				.also { found ->
					assertThat(found).hasSameElementsAs(cards)
					verify(storageService).findUser(123456)
					verify(storageService).getUserAvailableCards(123456)
				}
	}

	@Test
	fun testGetUserAvailableCardsEmpty() {
		val usersService = UsersService(randomService, storageService)
		val user = User(123456, "vegeta")
		whenever(storageService.findUser(123456)).doReturn(user)
		usersService
				.getCardsOf(123456)
				.also { cards ->
					assertThat(cards).isEmpty()
					verify(storageService).findUser(123456)
				}
	}

	@Test
	fun testCreateTeam() {
		val userService = UsersService(randomService, storageService)
		val superheroes = listOf(1, 2, 3, 4)
		val teamData = UserTeamData(123456, superheroes)
		val user = User(123456, "vegeta")
		whenever(randomService.generate(10000)).doReturn(45)
		whenever(storageService.findUser(123456)).doReturn(user)
		userService
				.createTeam(teamData)
				.also { teamCreated ->
					assertThat(teamCreated.id).isEqualTo(45)
					assertThat(teamCreated.superheroes).hasSameElementsAs(superheroes)
					verify(storageService).findUser(123456)
					verify(storageService).storeUserTeam(123456, teamCreated)
				}
	}

}

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TeamsServiceTest {

	@Mock
	private lateinit var marvelService: MarvelService

	@Mock
	private lateinit var storageService: StorageService

	@Test
	fun testGetTeam() {
		val teamsService = TeamsService(marvelService, storageService)
		val teamId = 45
		val team = Team(teamId, listOf(1, 2, 3, 4))
		val hulk = Card(1, "hulk", "big", "hulk.jpg", 23)
		val ironMan = Card(2, "iron man", "metal", "iron_man.jpg", 45)
		val captainAmerica = Card(3, "captain america", "shield", "captain_america.jpg", 80)
		val spiderMan = Card(4, "spider man", "web", "spider_man.jpg", 67)
		whenever(storageService.findTeam(45)).doReturn(team)
		whenever(marvelService.getCardOf(1)).doReturn(hulk)
		whenever(marvelService.getCardOf(2)).doReturn(ironMan)
		whenever(marvelService.getCardOf(3)).doReturn(captainAmerica)
		whenever(marvelService.getCardOf(4)).doReturn(spiderMan)
		teamsService
				.getTeam(45)
				.also { cards ->
					assertThat(cards).contains(hulk).contains(ironMan).contains(captainAmerica).contains(spiderMan)
					verify(storageService).findTeam(45)
					verify(marvelService).getCardOf(1)
					verify(marvelService).getCardOf(2)
					verify(marvelService).getCardOf(3)
					verify(marvelService).getCardOf(4)
				}
	}

	@Test
	fun testTeamNotFound() {
		val teamsService = TeamsService(marvelService, storageService)
		whenever(storageService.findTeam(45)).doReturn(null)
		assertThrows<TeamNotFoundException> { teamsService.getTeam(45) }
				.also { exception ->
					assertThat(exception.message).isEqualTo("team 45 not found")
					verify(storageService).findTeam(45)
					verify(marvelService, never()).getCardOf(any())
				}
	}

}

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FightServiceTest {

	@Mock
	private lateinit var usersService: UsersService

	@Mock
	private lateinit var storageService: StorageService

	@Mock
	private lateinit var randomService: RandomService

	@Mock
	private lateinit var marvelService: MarvelService

	@Test
	fun testFight() {
		val fightService = FightService(marvelService, usersService, storageService, randomService)
		val geolocation = Geolocation(latitude = 1231231, amplitude = 53234)
		val userId = 123456
		val opponentId = 100000
		val data = FightData(user_id = userId, geolocation = geolocation, timestamp = 1500000034)
		val userTeam = Team(23, listOf(1, 2, 3, 4))
		val opponentTeam = Team(45, listOf(5, 6, 7, 8))
		val hulk = Card(1, "hulk", "big", "hulk.jpg", 23)
		val ironMan = Card(2, "iron man", "metal", "iron_man.jpg", 45)
		val captainAmerica = Card(3, "captain america", "shield", "captain_america.jpg", 80)
		val spiderMan = Card(4, "spider man", "web", "spider_man.jpg", 67)
		val thor = Card(5, "thor", "hammer", "thor.jpg", 89)
		val drStrange = Card(6, "dr strange", "time", "dr_strange.jpg", 81)
		val antMan = Card(7, "ant man", "ants", "ant_man.jpg", 55)
		val falcon = Card(8, "falcon", "wings", "falcon.jpg", 32)
		whenever(usersService.getUser(userId)).doReturn(User(userId, "vegeta"))
		whenever(usersService.getUser(opponentId)).doReturn(User(opponentId, "goku"))
		whenever(storageService.findAnOpponentFor(userId)).doReturn(opponentId)
		whenever(storageService.findUserTeam(userId)).doReturn(userTeam)
		whenever(storageService.findUserTeam(opponentId)).doReturn(opponentTeam)
		whenever(marvelService.getCardOf(1)).doReturn(hulk)
		whenever(marvelService.getCardOf(2)).doReturn(ironMan)
		whenever(marvelService.getCardOf(3)).doReturn(captainAmerica)
		whenever(marvelService.getCardOf(4)).doReturn(spiderMan)
		whenever(marvelService.getCardOf(5)).doReturn(thor)
		whenever(marvelService.getCardOf(6)).doReturn(drStrange)
		whenever(marvelService.getCardOf(7)).doReturn(antMan)
		whenever(marvelService.getCardOf(8)).doReturn(falcon)
		whenever(randomService.generate(10000)).doReturn(1)
		fightService
				.fight(data)
				.also { fightResult ->
					assertThat(fightResult.id).isEqualTo(1)
					assertThat(fightResult.winner).isEqualTo("goku")
					assertThat(fightResult.players[0]).isEqualTo(Player(userId, "vegeta", 23, 215))
					assertThat(fightResult.players[1]).isEqualTo(Player(opponentId, "goku", 45, 257))
					verify(usersService).getUser(userId)
					verify(usersService).getUser(opponentId)
					verify(storageService).findAnOpponentFor(userId)
					verify(storageService).findUserTeam(userId)
					verify(storageService).findUserTeam(opponentId)
					verify(marvelService).getCardOf(1)
					verify(marvelService).getCardOf(2)
					verify(marvelService).getCardOf(3)
					verify(marvelService).getCardOf(4)
					verify(marvelService).getCardOf(5)
					verify(marvelService).getCardOf(6)
					verify(marvelService).getCardOf(7)
					verify(marvelService).getCardOf(8)
					verify(randomService).generate(10000)
				}
	}

}
