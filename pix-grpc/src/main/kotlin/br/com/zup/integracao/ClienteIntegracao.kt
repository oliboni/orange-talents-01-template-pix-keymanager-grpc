package br.com.zup.integracao

import br.com.zup.contaCliente.ContaClienteResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("\${erpItau.server.account}")
interface ClienteIntegracao{

    @Get("/{clientId}/contas?tipo={tipo}")
    fun consultaConta(@PathVariable clientId:String, @QueryValue tipo:String ):HttpResponse<ContaClienteResponse>

}