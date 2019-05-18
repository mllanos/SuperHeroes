package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import net.spy.memcached.AddrUtil
import net.spy.memcached.ConnectionFactoryBuilder
import net.spy.memcached.ConnectionFactoryBuilder.Protocol
import net.spy.memcached.MemcachedClient
import net.spy.memcached.auth.AuthDescriptor
import net.spy.memcached.auth.PlainCallbackHandler
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class DefaultConfig {

	@Bean
	fun gson() = GsonBuilder()
			.serializeNulls()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.create()!!

	@Bean
	fun memcachedClient(): MemcachedClient {
		val ad = AuthDescriptor(arrayOf("PLAIN"), PlainCallbackHandler(
				System.getenv("MEMCACHEDCLOUD_USERNAME"),
				System.getenv("MEMCACHEDCLOUD_PASSWORD")))
		val addresses = AddrUtil.getAddresses(System.getenv("MEMCACHEDCLOUD_SERVERS"))
		return MemcachedClient(
				ConnectionFactoryBuilder().setProtocol(Protocol.BINARY).setAuthDescriptor(ad).build(),
				addresses)
	}

	@Bean
	fun restTemplate() = RestTemplateBuilder().build()

	@Bean
	fun superheroesRepository() = SuperheroesRepository.get()

}
