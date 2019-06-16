package ar.edu.utn.frba.mobile.a2019c1.superheroes.services

import android.content.Context
import ar.edu.utn.frba.mobile.a2019c1.superheroes.domain.User

class SessionsService(context: Context) {

	private val storageService = StorageService(context)

	fun isUserLoggedIn() = storageService.getUser() != null

	fun getLoggedUser() = storageService.getUser()

	fun getCurrentTimer() = storageService.getTimer()

	fun storeTimer(timer: Long) = storageService.storeTimer(timer)

	fun createSession(user: User) = storageService.storeUser(user)

	fun logout() = storageService.deleteUser()

	fun storeLoggedUserTeamId(teamId: Int) = storageService.storeTeamId(teamId)

	fun getLoggedUserTeamId() = storageService.getTeamId()

}
