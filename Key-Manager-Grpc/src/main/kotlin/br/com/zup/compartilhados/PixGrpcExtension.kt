package br.com.zup.compartilhados

import br.com.zup.ConsultaByClienteRequest
import br.com.zup.ConsultaChaveRequest
import br.com.zup.ConsultaChaveRequest.FiltroCase.*
import br.com.zup.NovaChaveRequest
import br.com.zup.RemoveChaveRequest
import br.com.zup.TipoChave.UNKNOWN_KEY_TYPE
import br.com.zup.TipoConta.UNKNOWN_ACCOUNT_TYPE
import br.com.zup.pix.ChavePixRequest
import br.com.zup.pix.consultas.ConsultaChave
import br.com.zup.pix.RemoveChavePixRequest
import br.com.zup.pix.consultas.ConsultaChaveByClienteRequest
import io.micronaut.validation.validator.Validator
import javax.validation.ConstraintViolationException

fun NovaChaveRequest.toModel(): ChavePixRequest {
    return ChavePixRequest(
        clienteId = clienteId,
        tipoChave = when (tipoChave) {
            UNKNOWN_KEY_TYPE -> null
            else -> TipoDaChave.valueOf(tipoChave.name)
        },
        valorChave = valorChave,
        tipoConta = when (tipoConta) {
            UNKNOWN_ACCOUNT_TYPE -> null
            else -> TipoDaConta.valueOf(tipoConta.name)

        }
    )
}

fun RemoveChaveRequest.toModel(): RemoveChavePixRequest {
    return RemoveChavePixRequest(
        chavePixId = chavePixId,
        clienteId = clienteId
    )
}

fun ConsultaChaveRequest.toModel(validator: Validator): ConsultaChave {
    return when(filtroCase){
        CHAVEPIXID -> chavePixId.let {
            ConsultaChave.PorChavePixId(it.pixId,it.clienteId)
        }
        CHAVE -> ConsultaChave.PorChave(chave)
        FILTRO_NOT_SET -> ConsultaChave.Invalida()
    }.also { filtro->
        validator.validate(filtro).also {
            if (it.isNotEmpty()){
                throw ConstraintViolationException(it)
            }
        }
    }
}

fun ConsultaByClienteRequest.toModel():ConsultaChaveByClienteRequest{
    return ConsultaChaveByClienteRequest(clienteId = clienteId)
}