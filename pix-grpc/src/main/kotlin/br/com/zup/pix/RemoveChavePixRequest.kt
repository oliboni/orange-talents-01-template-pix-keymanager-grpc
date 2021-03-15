package br.com.zup.pix

import br.com.zup.compartilhados.validadores.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
class RemoveChavePixRequest(
    @field:NotBlank
    @field:ValidUUID
    val chavePix: String,

    @field:NotBlank
    @field:ValidUUID
    val clienteId: String
) {

}