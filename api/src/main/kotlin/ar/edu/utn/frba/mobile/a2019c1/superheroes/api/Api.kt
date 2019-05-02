package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/superheroes")
class UsersController(private val usersService: UsersService) {

    @PostMapping("/users")
    fun createUser(@RequestBody userData: UserData): UserResponseResource {
        return usersService
                .createWith(userData)
                .asResource()
    }

}
