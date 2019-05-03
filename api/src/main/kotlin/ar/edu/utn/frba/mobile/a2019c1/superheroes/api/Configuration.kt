package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import net.spy.memcached.MemcachedClient
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.InetSocketAddress

@Configuration
class DefaultConfig {

	@Bean
	fun gson() = GsonBuilder()
			.serializeNulls()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.create()!!

	@Bean
	fun memcachedClient() = MemcachedClient(InetSocketAddress("127.0.0.1", 11211))

	@Bean
	fun restTemplate() = RestTemplateBuilder().build()

	@Bean
	fun superheroesRepository() = SuperheroesRepository.get()

}
