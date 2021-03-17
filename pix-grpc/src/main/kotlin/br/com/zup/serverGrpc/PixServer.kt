package br.com.zup.serverGrpc

import br.com.zup.*
import br.com.zup.compartilhados.exceptions.ErrorHandler
import br.com.zup.compartilhados.toModel
import br.com.zup.integracao.BcbClient
import br.com.zup.pix.ChavePixRepository
import br.com.zup.pix.ChavePixServer
import io.grpc.stub.StreamObserver
import io.micronaut.validation.validator.Validator
import javax.inject.Singleton

@ErrorHandler
@Singleton
class PixServer(
    val chavePixServer: ChavePixServer,
    val bcbClient: BcbClient,
    val chavePixRepository: ChavePixRepository,
    val validator: Validator
) : PixServiceGrpc.PixServiceImplBase() {

    override fun registro(
        request: NovaChaveRequest?,
        responseObserver: StreamObserver<NovaChaveResponse>?
    ) {
        val response = chavePixServer.registraChave(request!!.toModel())

        responseObserver!!.onNext(
            NovaChaveResponse.newBuilder()
                .setClienteId(response.clienteId.toString())
                .setPixId(response.id.toString())
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun remove(
        request: RemoveChaveRequest?,
        responseObserver: StreamObserver<RemoveChaveResponse>?
    ) {
        responseObserver!!.onNext(chavePixServer.removeChave(request!!.toModel()))
        responseObserver.onCompleted()
    }

    override fun consulta(
        request: ConsultaChaveRequest?,
        responseObserver: StreamObserver<ConsultaChaveRespose>?
    ) {
        val response = request!!.toModel(validator).filtra(chavePixRepository, bcbClient).converter()
        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }

    override fun consultaByCliente(
        request: ConsultaByClienteRequest?,
        responseObserver: StreamObserver<ConsultaByClienteResponse>?
    ) {
        responseObserver!!.onNext(chavePixServer.consultaChaveCliente(request!!.toModel()))
        responseObserver.onCompleted()
    }
}