package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
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
		val user = User(id, "vegeta")
		whenever(usersService.createWith(any())).thenReturn(user)
		mockMvc.perform(post("/superheroes/users")
				.content(body.toString())
				.contentType(APPLICATION_JSON))
				.andExpect(status().isOk)
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("\$.id").value(id))
	}

	@Test
	fun testMissingNickname() {
		val body = JSONObject().put("nickname", null)
		whenever(usersService.createWith(any())).thenThrow(IllegalArgumentException("missing nickname"))
		mockMvc.perform(post("/superheroes/users")
				.content(body.toString())
				.contentType(APPLICATION_JSON))
				.andExpect(status().isBadRequest)
				.andExpect(jsonPath("\$.message").value("missing nickname"))
	}

}
