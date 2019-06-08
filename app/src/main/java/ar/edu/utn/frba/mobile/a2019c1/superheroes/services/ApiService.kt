package ar.edu.utn.frba.mobile.a2019c1.superheroes.services

import android.content.Context
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.Card
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.User
import com.android.volley.Request.Method.GET
import com.android.volley.Request.Method.POST
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import org.json.JSONObject
import java.util.Date

private const val API_BASE_URL = "https://superheroes-mobile-api.herokuapp.com/superheroes"

class ApiService(private val context: Context) {

	private val gson = Gson()

	fun createUser(nickname: String, responseHandler: (User) -> Unit, errorHandler: (VolleyError) -> Unit) {
		val json = JSONObject().put("nickname", nickname)
		post(
			"/users", json, { response ->
				val id = response.getInt("id")
				val user = User(id, nickname)
				responseHandler(user)
			},
			errorHandler
		)
	}

	fun getUserAvailableCards(user: User, responseHandler: (List<Card>) -> Unit, errorHandler: (VolleyError) -> Unit) {
		get(
			"/users/${user.id}/available_cards", { response ->
				val resource = gson.fromJson(response.toString(), CardsResponseResource::class.java)
				responseHandler(resource.cards)
			},
			errorHandler
		)
	}

	fun openBundle(user: User, responseHandler: (List<Card>) -> Unit, errorHandler: (VolleyError) -> Unit) {
		get(
			"/cards?user_id=${user.id}&quantity=3", { response ->
				val resource = gson.fromJson(response.toString(), CardsResponseResource::class.java)
				responseHandler(resource.cards)
			},
			errorHandler
		)
	}

	private fun post(
		uri: String,
		body: JSONObject,
		responseHandler: (JSONObject) -> Unit,
		errorHandler: (VolleyError) -> Unit
	) {
		val request = JsonObjectRequest(POST, "$API_BASE_URL$uri", body,
			Response.Listener { response -> responseHandler(response) },
			Response.ErrorListener { error -> errorHandler(error) })
		VolleySingleton.getInstance(context).addToRequestQueue(request)

	}

	private fun get(uri: String, responseHandler: (JSONObject) -> Unit, errorHandler: (VolleyError) -> Unit) {
		val request = JsonObjectRequest(GET, "$API_BASE_URL$uri", null,
			Response.Listener { response -> responseHandler(response) },
			Response.ErrorListener { error -> errorHandler(error) })
		VolleySingleton.getInstance(context).addToRequestQueue(request)
	}

	data class CardsResponseResource(val cards: List<Card>)

}
