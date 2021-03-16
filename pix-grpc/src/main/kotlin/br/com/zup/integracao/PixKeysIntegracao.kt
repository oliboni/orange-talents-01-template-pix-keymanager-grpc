package br.com.zup.integracao

import br.com.zup.bcb.CreatePixKeyResponse
import br.com.zup.bcb.CreatePixRequest
import br.com.zup.bcb.DeletePixKeyRequest
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("\${bcb.pix.keys}")
interface PixKeysIntegracao {
    @Post(produces = [MediaType.APPLICATION_XML], consumes = [MediaType.APPLICATION_XML])
    fun registraChave(@Body request: CreatePixRequest): CreatePixKeyResponse

    @Delete("/{key}",produces = [MediaType.APPLICATION_XML], consumes = [MediaType.APPLICATION_XML])
    fun deletaChave(key: String,@Body request: DeletePixKeyRequest)
}