package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.common.io.Resources
import java.nio.charset.StandardCharsets
import kotlin.reflect.KClass

data class Superheroes(val superheroes: List<Superhero>)
data class Superhero(val id: Int, val name: String, val description: String, val thumbnail: String)

object SuperheroesRepository {
	fun get() = JSONMapper.map("superheroes.json", Superheroes::class).superheroes
	fun getTotal() = get().size
}

private object JSONMapper {
	private val mapper = jacksonObjectMapper()
			.registerModule(KotlinModule())
			.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)

	fun <T : Any> map(fileName: String, dto: KClass<T>): T =
			Resources.getResource(fileName)
					.let { Resources.toString(it, StandardCharsets.UTF_8) }
					.let { mapper.readValue(it, dto.java) }

}
