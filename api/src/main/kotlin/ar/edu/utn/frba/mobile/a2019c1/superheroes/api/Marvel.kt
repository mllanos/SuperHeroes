package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import org.springframework.stereotype.Service
import kotlin.streams.toList

@Service
class MarvelService(private val superheroesRepository: List<Superhero>, private val randomService: RandomService) {

	fun getCards(quantity: Int): List<Card> {
		val selectedSuperheroes = mutableListOf<Superhero>()
		for (x in 1..quantity) {
			val i = randomService.generate(SuperheroesRepository.getTotal())
			selectedSuperheroes.add(superheroesRepository[i])
		}
		return selectedSuperheroes.stream().map { it.asCard() }.toList()
	}

	fun getCardOf(superheroId: Int) = superheroesRepository.find { it.id == superheroId }!!.asCard()

	private fun Superhero.asCard() = Card(id, name, description, thumbnail, power)

}

