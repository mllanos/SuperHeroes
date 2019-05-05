package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/superheroes")
class UsersController(private val usersService: UsersService) {

	@PostMapping("/users")
	@ResponseStatus(code = HttpStatus.CREATED)
	fun createUser(@RequestBody userData: UserData): UserResponseResource {
		return usersService
				.createWith(userData)
				.asResource()
	}

	@GetMapping("/users/{id}/available_cards")
	fun getUserAvailableCards(@PathVariable id: String): CardsResponseResource {
		val userId = id.toInt()
		return usersService
				.getCardsOf(userId)
				.asResource()
	}

}

@RestController
@RequestMapping("/superheroes")
class CardsController(private val cardsService: CardsService) {

	@GetMapping("/cards")
	fun getCards(@RequestParam user_id: Int?, @RequestParam quantity: Int?): CardsResponseResource {
		val cardsData = CardsData(user_id, quantity)
		return cardsService
				.getBundle(cardsData)
				.asResource()
	}

}
