package br.com.zup.integracao

import br.com.zup.bcb.*
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("\${bcb.pix.keys}")
interface BcbClient {
    @Post(
        produces = [MediaType.APPLICATION_XML],
        consumes = [MediaType.APPLICATION_XML]
    )
    fun registraChave(@Body request: CreatePixRequest): HttpResponse<CreatePixKeyResponse?>

    @Delete(
        "/{key}",
        produces = [MediaType.APPLICATION_XML],
        consumes = [MediaType.APPLICATION_XML]
    )
    fun deletaChave(key: String, @Body request: DeletePixKeyRequest): HttpResponse<DeletePixKeyResponse>

    @Get(
        "/{key}",
        consumes = [MediaType.APPLICATION_XML]
    )
    fun consultaChave(key: String): HttpResponse<PixKeyDetailsResponse>
}