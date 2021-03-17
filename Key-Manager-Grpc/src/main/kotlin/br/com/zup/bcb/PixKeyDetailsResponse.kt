package br.com.zup.bcb

import br.com.zup.compartilhados.AccountType.*
import br.com.zup.compartilhados.Instituicoes
import br.com.zup.compartilhados.TipoDaChave
import br.com.zup.compartilhados.TipoDaConta.*
import br.com.zup.contaCliente.ContaCliente
import br.com.zup.pix.consultas.ConsultaChavePixResponse
import java.time.LocalDateTime

class PixKeyDetailsResponse(
    val keyType: TipoDaChave,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: LocalDateTime
) {
    fun toModel(): ConsultaChavePixResponse {
        return ConsultaChavePixResponse(
            tipoChave = keyType,
            chave = key,
            contaCliente = ContaCliente(
                tipoConta = when (bankAccount.accountType) {
                    CACC -> CONTA_CORRENTE
                    SVGS -> CONTA_POUPANCA
                },
                nomeInstituicao = Instituicoes.nome(bankAccount.participant),
                ispbInstituicao = bankAccount.participant,
                agencia = bankAccount.branch,
                numero = bankAccount.accountNumber,
                nomeTitular = owner.name,
                cpfTitular = owner.taxIdNumber
            ),
            criadoEm = createdAt
        )
    }
}