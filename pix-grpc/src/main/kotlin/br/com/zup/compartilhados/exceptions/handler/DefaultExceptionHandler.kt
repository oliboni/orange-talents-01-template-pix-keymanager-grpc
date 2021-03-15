package br.com.zup.compartilhados.exceptions.handler

import br.com.zup.compartilhados.exceptions.ExceptionHandler
import br.com.zup.compartilhados.exceptions.StatusWithDatails
import io.grpc.Status
import io.micronaut.http.client.exceptions.HttpClientResponseException

class DefaultExceptionHandler : ExceptionHandler<Exception> {
    override fun handle(e: Exception): StatusWithDatails {
        when(e){
            is IllegalArgumentException -> Status.INVALID_ARGUMENT.withDescription(e.message)
            is IllegalStateException -> Status.FAILED_PRECONDITION.withDescription(e.message)
            is HttpClientResponseException -> Status.INVALID_ARGUMENT.withDescription(e.message)
            else -> Status.UNKNOWN
        }.let { status->
            return StatusWithDatails(status.withCause(e))
        }
    }

    override fun supports(e: Exception): Boolean {
        return true
    }
}