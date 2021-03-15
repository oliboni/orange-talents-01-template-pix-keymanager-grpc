package br.com.zup.compartilhados.exceptions.handler

import br.com.zup.compartilhados.exceptions.ExceptionHandler
import br.com.zup.compartilhados.exceptions.NotFoundException
import br.com.zup.compartilhados.exceptions.StatusWithDatails
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class NotFoundExceptionHandler : ExceptionHandler<NotFoundException> {
    override fun handle(e: NotFoundException): StatusWithDatails {
        return StatusWithDatails(
            Status.NOT_FOUND
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is NotFoundException
    }
}