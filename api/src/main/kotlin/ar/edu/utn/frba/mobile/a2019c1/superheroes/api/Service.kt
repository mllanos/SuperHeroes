package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import ar.edu.utn.frba.mobile.a2019c1.superheroes.api.FightResult.Player
import org.springframework.stereotype.Service
import kotlin.streams.toList

private const val MAX_USER_ID_LENGTH = 1000000
private const val MAX_TEAM_ID_LENGTH = 10000
private const val MAX_FIGHT_ID_LENGTH = 10000

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

	fun getBundle(cardsData: CardsData): List<Card> = usersService.getUser(cardsData.userId!!)
			.let {
				val cards = marvelService.getCards(cardsData.quantity!!)
				storageService.updateUserAvailableCards(cardsData.userId, cards)
				return cards
			}

}

@Service
class TeamsService(private val marvelService: MarvelService, private val storageService: StorageService) {

	fun getTeam(teamId: Int) = storageService.findTeam(teamId)
			?.let { team ->
				team.superheroes.stream().map { marvelService.getCardOf(it) }.toList()
			} ?: throw TeamNotFoundException("team $teamId not found")

}

@Service
class FightService(private val marvelService: MarvelService,
				   private val usersService: UsersService,
				   private val storageService: StorageService,
				   private val randomService: RandomService) {

	fun fight(fightData: FightData): FightResult {
		val user = usersService.getUser(fightData.user_id)
		val team = storageService.findUserTeam(user.id)?.let { it }
				?: throw TeamNotFoundException("user ${user.id} cannot fight without a team")
		val opponent = storageService.findAnOpponentFor(user.id)
				?.let { id -> usersService.getUser(id) }
				?: throw OpponentNotFoundException("could not find an opponent for user: ${user.id}")
		val opponentTeam = storageService.findUserTeam(opponent.id)?.let { it }
				?: throw TeamNotFoundException("opponent ${opponent.id} cannot fight without a team")
		val teamTotalPower = team.superheroes.stream().map { marvelService.getCardOf(it) }.toList().sumBy { it.power }
		val opponentTeamTotalPower = opponentTeam.superheroes.stream().map { marvelService.getCardOf(it) }.toList().sumBy { it.power }
		val winner = if (teamTotalPower >= opponentTeamTotalPower) user else opponent
		val fightId = randomService.generate(MAX_FIGHT_ID_LENGTH)
		val fightResult = FightResult(
				id = fightId,
				winner = winner.nickname,
				players = listOf(
						Player(user.id, user.nickname, team.id, teamTotalPower),
						Player(opponent.id, opponent.nickname, opponentTeam.id, opponentTeamTotalPower)))
		storageService.storeFight(fightResult)
		return fightResult
	}

}
