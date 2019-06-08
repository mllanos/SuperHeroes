package ar.edu.utn.frba.mobile.a2019c1.superheroes.services

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.User
import com.google.gson.Gson

private const val SHARED_PREFERENCES_KEY = "STORAGE_SERVICE"
private const val LOGGED_USER = "USER_LOGGED"
private const val TIMER = "TIMER"

class StorageService(context: Context) {

	private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE)
	private val gson = Gson()

	fun storeUser(user: User) = storePreference(LOGGED_USER, user)

	fun storeTimer(timer: Long) = storePreference(TIMER, timer)

	fun getUser(): User? = getPreference(LOGGED_USER, User::class.java)

	fun getTimer(): Long = getPreference(TIMER, Long::class.java)

	fun deleteUser() = removePreference(LOGGED_USER)

	private fun storePreference(key: String, preference: Any) = sharedPreferences
		.edit()
		.putString(key, gson.toJson(preference))
		.apply()


	private fun <T> getPreference(key: String, preference: Class<T>) = sharedPreferences
		.getString(key, null)
		.let {
			gson.fromJson(it, preference)
		}

	private fun removePreference(key: String) = sharedPreferences
		.edit()
		.remove(key)
		.apply()

}
