package br.com.zup.compartilhados.exceptions.handler

import br.com.zup.compartilhados.exceptions.ExceptionHandler
import br.com.zup.compartilhados.exceptions.StatusWithDatails
import io.grpc.Status
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
class ConstraintViolationExceptionHandler: ExceptionHandler<ConstraintViolationException> {

    override fun handle(e: ConstraintViolationException): StatusWithDatails {
        return StatusWithDatails(
            Status.INVALID_ARGUMENT
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ConstraintViolationException
    }
}