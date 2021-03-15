package br.com.zup.compartilhados.exceptions.handler

import br.com.zup.compartilhados.exceptions.AlreadyExistsException
import br.com.zup.compartilhados.exceptions.ExceptionHandler
import br.com.zup.compartilhados.exceptions.StatusWithDatails
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class AlreadyExistsExceptionHandler : ExceptionHandler<AlreadyExistsException> {
    override fun handle(e: AlreadyExistsException): StatusWithDatails {
        return StatusWithDatails(
            Status.ALREADY_EXISTS
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is AlreadyExistsException
    }
}