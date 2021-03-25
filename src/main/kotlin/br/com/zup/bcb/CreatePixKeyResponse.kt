package br.com.zup.bcb

import br.com.zup.compartilhados.TipoDaChave
import br.com.zup.contaCliente.ContaCliente
import br.com.zup.pix.ChavePix
import java.time.LocalDateTime
import java.util.*

class CreatePixKeyResponse(
    val keyType: String,
    val key: String,
    val createdAt: LocalDateTime
) {
    fun toModel(clienteid: UUID, contaCliente: ContaCliente): ChavePix {
        return ChavePix(
            clienteid,
            TipoDaChave.valueOf(keyType),
            key,
            contaCliente,
            createdAt
        )
    }
}
