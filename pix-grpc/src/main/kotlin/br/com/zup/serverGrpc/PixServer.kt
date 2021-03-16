package br.com.zup.serverGrpc

import br.com.zup.*
import br.com.zup.compartilhados.exceptions.ErrorHandler
import br.com.zup.compartilhados.toModel
import br.com.zup.pix.ChavePixServer
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@ErrorHandler
@Singleton
class PixServer(val chavePixServer: ChavePixServer): PixServiceGrpc.PixServiceImplBase(){

    override fun registro(request: NovaChaveRequest?, responseObserver: StreamObserver<NovaChaveResponse>?) {
        val chavePixRequest = request!!.toModel()
        val response = chavePixServer.registraChave(chavePixRequest)

        responseObserver!!.onNext(NovaChaveResponse.newBuilder()
            .setClienteId(response.clienteId.toString())
            .setPixId(response.id.toString())
            .build())
        responseObserver.onCompleted()
    }

    override fun remove(request: RemoveChaveRequest?, responseObserver: StreamObserver<RemoveChaveResponse>?) {
        val removeChaveRequest = request!!.toModel()

        responseObserver!!.onNext(chavePixServer.removeChave(removeChaveRequest))
        responseObserver.onCompleted()
    }
}