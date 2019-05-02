package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
class RandomService {

    private val random: SecureRandom = SecureRandom()

    fun generate(bound: Int) = random.nextInt(bound)

}
