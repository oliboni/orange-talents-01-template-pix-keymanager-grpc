package br.com.zup.pix.consultas

import br.com.zup.compartilhados.validadores.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class ConsultaChaveByClienteRequest(
    @field:ValidUUID
    @field:NotBlank
    val clienteId: String
)