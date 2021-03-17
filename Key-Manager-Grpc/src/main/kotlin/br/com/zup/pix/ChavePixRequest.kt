package br.com.zup.pix

import br.com.zup.compartilhados.TipoDaChave
import br.com.zup.compartilhados.TipoDaConta
import br.com.zup.compartilhados.validadores.UniqueValue
import br.com.zup.compartilhados.validadores.ValidPixKey
import br.com.zup.compartilhados.validadores.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ValidPixKey
@Introspected
class ChavePixRequest(
    @field:NotBlank
    @field:ValidUUID
    val clienteId: String?,

    @field:NotNull
    val tipoChave: TipoDaChave?,

//    @field:UniqueValue(domainClassName = "ChavePix",field = "chave", message = "Chave já cadastrada!")
    val valorChave: String?,

    @field:NotNull
    val tipoConta: TipoDaConta?
)