package ar.edu.utn.frba.mobile.a2019c1.superheroes.services

import android.content.Context
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.User
import com.android.volley.Request.Method.POST
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

private const val API_BASE_URL = "https://superheroes-mobile-api.herokuapp.com/superheroes"

class ApiService(private val context: Context) {

	fun createUser(
		nickname: String,
		responseHandler: (User) -> Unit,
		errorHandler: (VolleyError) -> Unit
	) {
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

}
