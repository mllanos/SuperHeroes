package ar.edu.utn.frba.mobile.a2019c1.superheroes.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

class UserNotFoundException(message: String?) : RuntimeException(message)
class TeamNotFoundException(message: String?) : RuntimeException(message)
class OpponentNotFoundException(message: String?) : RuntimeException(message)

@ControllerAdvice
class ApiExceptionHandler : ResponseEntityExceptionHandler() {

	@ExceptionHandler(value = [IllegalArgumentException::class, IllegalStateException::class])
	fun handleIllegal(e: RuntimeException) = badRequestEntity(e)

	@ExceptionHandler(value = [UserNotFoundException::class])
	fun handleUsers(e: RuntimeException) = notFoundEntity(e)

	@ExceptionHandler(value = [TeamNotFoundException::class])
	fun handleTeams(e: RuntimeException) = notFoundEntity(e)

	@ExceptionHandler(value = [OpponentNotFoundException::class])
	fun handleFights(e: RuntimeException) = notFoundEntity(e)

	private fun badRequestEntity(e: RuntimeException) = ResponseEntity(
			ErrorMessage(
					message = e.message,
					error = HttpStatus.BAD_REQUEST.name.toLowerCase(),
					status = HttpStatus.BAD_REQUEST.value(),
					cause = e.cause),
			HttpStatus.BAD_REQUEST)

	private fun notFoundEntity(e: RuntimeException) = ResponseEntity(
			ErrorMessage(
					message = e.message,
					error = HttpStatus.NOT_FOUND.name.toLowerCase(),
					status = HttpStatus.NOT_FOUND.value(),
					cause = e.cause),
			HttpStatus.NOT_FOUND)

	private fun internalServerErrorEntity(e: RuntimeException) = ResponseEntity(
			ErrorMessage(
					message = e.message,
					error = HttpStatus.INTERNAL_SERVER_ERROR.name.toLowerCase(),
					status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
					cause = e.cause),
			HttpStatus.INTERNAL_SERVER_ERROR)

	data class ErrorMessage(val message: String?, val error: String?, val status: Int, val cause: Throwable?)

}
