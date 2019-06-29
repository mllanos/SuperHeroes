package ar.edu.utn.frba.mobile.a2019c1.superheroes.services

import android.content.Context
import android.location.Location
import android.util.Log
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.Card
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.Team
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.User
import com.android.volley.Request.Method.GET
import com.android.volley.Request.Method.POST
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import com.android.volley.DefaultRetryPolicy


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

	fun createTeam(
		user: User,
		cards: List<Card>,
		responseHandler: (Int) -> Unit,
		errorHandler: (VolleyError) -> Unit
	) {
		val json = JSONObject().put("superheroes", JSONArray(cards.map { it.id }.toList()))
		post(
			"/users/${user.id}/team", json, { response ->
				val id = response.getInt("id")
				responseHandler(id)
			},
			errorHandler
		)
	}

	fun getTeam(id: Int, responseHandler: (Team) -> Unit, errorHandler: (VolleyError) -> Unit) {
		get(
			"/teams/$id", { response ->
				val resource = gson.fromJson(response.toString(), TeamResponseResource::class.java)
				val team = Team(id, resource.superheroes, resource.total_power)
				responseHandler(team)
			}, errorHandler
		)
	}

	fun startFight(
		userId: Int,
		location: Location?,
		timestamp: Long,
		responseHandler: (Int) -> Unit,
		errorHandler: (VolleyError) -> Unit
	) {
		val json = JSONObject().put("user_id", userId)
			.put(
				"geolocation",
				JSONObject().put("latitude", location?.latitude ?: 0.0).put("longitude", location?.longitude ?: 0.0)
			)
			.put("timestamp", timestamp)

		Log.d("DATA", json.toString())

		post(
			"/fight", json, { response ->
				Log.d("RESPONSE", response.toString())
				val id = response.getInt("id")
				responseHandler(id)
			},
			errorHandler,
			30000
		)
	}

	private fun post(
		uri: String,
		body: JSONObject,
		responseHandler: (JSONObject) -> Unit,
		errorHandler: (VolleyError) -> Unit,
		initialTimeoutMs: Int? = null
	) {
		val request = JsonObjectRequest(POST, "$API_BASE_URL$uri", body,
			Response.Listener { response -> responseHandler(response) },
			Response.ErrorListener { error -> errorHandler(error) })
		VolleySingleton.getInstance(context).addToRequestQueue(request)

		if (initialTimeoutMs != null) {
			request.retryPolicy = DefaultRetryPolicy(
				initialTimeoutMs,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
			)
		}
	}

	private fun get(
		uri: String,
		responseHandler: (JSONObject) -> Unit,
		errorHandler: (VolleyError) -> Unit,
		initialTimeoutMs: Int? = null
	) {
		val request = JsonObjectRequest(GET, "$API_BASE_URL$uri", null,
			Response.Listener { response -> responseHandler(response) },
			Response.ErrorListener { error -> errorHandler(error) })

		if (initialTimeoutMs != null) {
			request.retryPolicy = DefaultRetryPolicy(
				initialTimeoutMs,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
			)
		}

		VolleySingleton.getInstance(context).addToRequestQueue(request)
	}

	data class CardsResponseResource(val cards: List<Card>)

	data class TeamResponseResource(val superheroes: List<Card>, val total_power: Int)

}
