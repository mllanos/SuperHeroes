package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import org.springframework.stereotype.Service

@Service
class UsersService(private val randomService: RandomService, private val storageService: StorageService) {

	fun createWith(userData: UserData): User {
		requireNotNull(userData.nickname) { "missing nickname" }
		val userId = randomService.generate(1000000)
		val user = User(userId, userData.nickname)
		storageService.storeUser(user)
		return user
	}

}
