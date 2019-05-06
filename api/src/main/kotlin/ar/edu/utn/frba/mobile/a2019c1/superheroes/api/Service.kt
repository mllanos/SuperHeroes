package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import org.springframework.stereotype.Service
import kotlin.streams.toList

private const val MAX_USER_ID_LENGTH = 1000000

private const val MAX_TEAM_ID_LENGTH = 10000

@Service
class UsersService(private val randomService: RandomService, private val storageService: StorageService) {

	fun createWith(userData: UserData): User {
		requireNotNull(userData.nickname) { "missing nickname" }
		val userId = randomService.generate(MAX_USER_ID_LENGTH)
		val user = User(userId, userData.nickname)
		storageService.storeUser(user)
		return user
	}

	fun getUser(id: Int) = storageService
			.findUser(id)?.let { it } ?: throw UserNotFoundException("user $id not found")

	fun getCardsOf(id: Int) = getUser(id)
			.let {
				storageService.getUserAvailableCards(id)
			}

	fun createTeam(teamData: UserTeamData) = getUser(teamData.userId)
			.let {
				val teamId = randomService.generate(MAX_TEAM_ID_LENGTH)
				val team = Team(teamId, teamData.superheroes)
				storageService.storeUserTeam(teamData.userId, team)
				return@let team
			}

}

@Service
class CardsService(private val usersService: UsersService,
				   private val marvelService: MarvelService,
				   private val storageService: StorageService) {

	fun getBundle(cardsData: CardsData): List<Card> {
		usersService.getUser(cardsData.userId!!).let {
			val cards = marvelService.getCards(cardsData.quantity!!)
			storageService.updateUserAvailableCards(cardsData.userId, cards)
			return cards
		}
	}

}

@Service
class TeamsService(private val marvelService: MarvelService, private val storageService: StorageService) {

	fun getTeam(teamId: Int) = storageService.findTeam(teamId)
			?.let { team ->
				team.superheroes.stream().map { marvelService.getCardOf(it) }.toList()
			} ?: throw TeamNotFoundException("team $teamId not found")

}
