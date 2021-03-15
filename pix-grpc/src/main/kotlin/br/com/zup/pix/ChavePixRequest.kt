package br.com.zup.pix

import br.com.zup.compartilhados.TipoDaChave
import br.com.zup.compartilhados.TipoDaConta
import br.com.zup.compartilhados.validadores.ValidPixKey
import br.com.zup.compartilhados.validadores.ValidUUID
import br.com.zup.contaCliente.ContaCliente
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ValidPixKey
@Introspected
class ChavePixRequest(
    @field:NotBlank
    @ValidUUID
    val clienteId: String?,

    @field:NotNull
    val tipoChave: TipoDaChave?,

    @field:NotBlank
//    @field:UniqueValue(domainClassName = "ChavePix",field = "chave")
    val valorChave: String?,

    @field:NotNull
    val tipoConta: TipoDaConta?
) {

    fun toModel(@Valid contaCliente: ContaCliente): ChavePix {
        return ChavePix(
            clienteId = UUID.fromString(clienteId),
            tipoChave = TipoDaChave.valueOf(tipoChave!!.name),
            chave = if (tipoChave == TipoDaChave.RANDOM) UUID.randomUUID().toString() else valorChave!!,
            contaCliente = contaCliente
        )
    }
}