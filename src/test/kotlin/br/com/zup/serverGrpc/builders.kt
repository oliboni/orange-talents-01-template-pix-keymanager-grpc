package br.com.zup.serverGrpc

import br.com.zup.NovaChaveRequest
import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.bcb.CreatePixKeyResponse
import br.com.zup.bcb.CreatePixRequest
import br.com.zup.compartilhados.Instituicoes
import br.com.zup.compartilhados.TipoDaChave
import br.com.zup.compartilhados.TipoDaConta
import br.com.zup.compartilhados.toModel
import br.com.zup.contaCliente.ContaCliente
import br.com.zup.contaCliente.ContaClienteResponse
import br.com.zup.contaCliente.Instituicao
import br.com.zup.contaCliente.Titular
import br.com.zup.pix.ChavePix
import java.time.LocalDateTime
import java.util.*

internal fun buildChavePix(
    clienteId: UUID,
    tipoChave: TipoDaChave = TipoDaChave.CPF,
    chave: String = "04585079033",
    contaCliente: ContaCliente = buildContaClienteResponse().toModel()
) =
    ChavePix(
        clienteId = clienteId,
        tipoChave = tipoChave,
        chave = chave,
        contaCliente = contaCliente,
        criadaEm = LocalDateTime.now()
    )

internal fun buildCreatePixKeyResponse(
    keyType: TipoChave = TipoChave.CPF,
    key: String = "04585079033"
) =
    CreatePixKeyResponse(
        keyType = keyType.toString(),
        key = key,
        createdAt = LocalDateTime.now()
    )

internal fun buildCreatePixKeyRequest(
    keyType: String = TipoChave.CPF.name,
    key: String = "04585079033",
    contaCliente: ContaClienteResponse = buildContaClienteResponse()
): CreatePixRequest {
    return CreatePixRequest(
        KeyType = keyType,
        Key = key,
        contaCliente = contaCliente,
    )
}

internal fun buildNovaChaveRequest(
    clienteId: UUID,
    tipoChave: TipoChave = TipoChave.CPF,
    valorChave: String = "04585079033",
    tipoConta: TipoConta = TipoConta.CONTA_CORRENTE
) =
    NovaChaveRequest.newBuilder()
        .setClienteId(clienteId.toString())
        .setTipoChave(tipoChave)
        .setValorChave(valorChave)
        .setTipoConta(tipoConta)
        .build()

internal fun buildContaClienteResponse(
    tipo: TipoDaConta = TipoDaConta.CONTA_CORRENTE,
    instituicao: Instituicao = Instituicao(Instituicoes.nome("60394079"), "60394079"),
    agencia: String = "0001",
    numero: String = "000001",
    titular: Titular = Titular("123", "Rodrigo Oliboni", "04585079033")
) =
    ContaClienteResponse(
        tipo = tipo,
        instituicao = instituicao,
        agencia = agencia,
        numero = numero,
        titular = titular
    )