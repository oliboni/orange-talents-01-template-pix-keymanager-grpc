package br.com.zup.compartilhados.exceptions

import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto

interface ExceptionHandler<E: Exception> {
    fun handle(e: E): StatusWithDatails
    fun supports(e: Exception): Boolean
}

data class StatusWithDatails(val status: Status, val metadata: Metadata = Metadata()) {
    constructor(se: StatusRuntimeException): this(se.status, se.trailers ?: Metadata())
    constructor(sp: com.google.rpc.Status): this(StatusProto.toStatusRuntimeException(sp))

    fun asRuntimeException(): StatusRuntimeException{
        return status.asRuntimeException(metadata)
    }
}
