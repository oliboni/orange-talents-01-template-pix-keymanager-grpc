package br.com.zup.serverGrpc

import br.com.zup.NovaChaveRequest
import br.com.zup.NovaChaveResponse
import br.com.zup.PixServiceGrpc
import br.com.zup.compartilhados.exceptions.ErrorHandler
import br.com.zup.compartilhados.toModel
import br.com.zup.contaCliente.ContaClienteServer
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@ErrorHandler
@Singleton
class PixServer(val contaClienteServer: ContaClienteServer): PixServiceGrpc.PixServiceImplBase(){

    override fun registro(request: NovaChaveRequest?, responseObserver: StreamObserver<NovaChaveResponse>?) {
        val chavePixRequest = request!!.toModel()
        val response = contaClienteServer.buscaContaCliente(chavePixRequest)

        responseObserver!!.onNext(NovaChaveResponse.newBuilder()
            .setClienteId(response.clienteId.toString())
            .setPixId(response.id.toString())
            .build())
        responseObserver.onCompleted()
    }
}