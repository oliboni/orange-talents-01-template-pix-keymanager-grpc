package br.com.zup.compartilhados

import br.com.zup.NovaChaveRequest
import br.com.zup.RemoveChaveRequest
import br.com.zup.TipoChave.UNKNOWN_KEY_TYPE
import br.com.zup.TipoConta.UNKNOWN_ACCOUNT_TYPE
import br.com.zup.pix.ChavePixRequest
import br.com.zup.pix.RemoveChavePixRequest

fun NovaChaveRequest.toModel(): ChavePixRequest {
    return ChavePixRequest(
        clienteId = clienteId,
        tipoChave = when (tipoChave) {
            UNKNOWN_KEY_TYPE -> null
            else -> TipoDaChave.valueOf(tipoChave.name)
        }!!,
        valorChave = valorChave,
        tipoConta = when (tipoConta) {
            UNKNOWN_ACCOUNT_TYPE -> null
            else -> TipoDaConta.valueOf(tipoConta.name)

        }!!
    )
}

fun RemoveChaveRequest.toModel(): RemoveChavePixRequest {
    return RemoveChavePixRequest(
        chavePix = chavePix,
        clienteId = clienteId
    )
}