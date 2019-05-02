package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import net.spy.memcached.MemcachedClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.net.InetSocketAddress

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

}

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

}
