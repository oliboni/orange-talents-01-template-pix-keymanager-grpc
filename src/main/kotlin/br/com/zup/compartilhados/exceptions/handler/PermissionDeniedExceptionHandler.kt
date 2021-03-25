package br.com.zup.compartilhados.exceptions.handler

import br.com.zup.compartilhados.exceptions.ExceptionHandler
import br.com.zup.compartilhados.exceptions.PermissionDeniedException
import br.com.zup.compartilhados.exceptions.StatusWithDatails
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class PermissionDeniedExceptionHandler: ExceptionHandler<PermissionDeniedException> {
    override fun handle(e: PermissionDeniedException): StatusWithDatails {
        return StatusWithDatails(
            Status.PERMISSION_DENIED
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is PermissionDeniedException
    }
}