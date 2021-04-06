package br.com.zup.bcb

import br.com.zup.compartilhados.AccountType
import br.com.zup.compartilhados.AccountType.CACC
import br.com.zup.compartilhados.AccountType.SVGS
import br.com.zup.compartilhados.TipoDaConta.CONTA_CORRENTE
import br.com.zup.compartilhados.TipoDaConta.CONTA_POUPANCA
import br.com.zup.contaCliente.ContaCliente
import br.com.zup.contaCliente.ContaClienteResponse

data class CreatePixRequest(
    val KeyType: String,
    val Key: String?,
    val contaCliente: ContaClienteResponse
) {
    val bankAccount = BankAccount(
        participant = contaCliente.instituicao.ispb,
        branch = contaCliente.agencia,
        accountNumber = contaCliente.numero,
        accountType = when (contaCliente.tipo) {
            CONTA_CORRENTE -> CACC
            CONTA_POUPANCA -> SVGS
        }
    )
    val owner = Owner(
        type = Owner.OwnerType.NATURAL_PERSON,
        name = contaCliente.titular.nome,
        taxIdNumber = contaCliente.titular.cpf
    )
}

data class Owner(
    val type: OwnerType,
    val name: String,
    val taxIdNumber: String
) {
    enum class OwnerType {
        NATURAL_PERSON,
        LEGAL_PERSON
    }
}

data class BankAccount(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: AccountType
)