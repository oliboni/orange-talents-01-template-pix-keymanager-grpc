package br.com.zup.bcb

import br.com.zup.compartilhados.AccountType
import br.com.zup.compartilhados.AccountType.CACC
import br.com.zup.compartilhados.AccountType.SVGS
import br.com.zup.compartilhados.TipoDaConta.CONTA_CORRENTE
import br.com.zup.compartilhados.TipoDaConta.CONTA_POUPANCA
import br.com.zup.contaCliente.ContaCliente

class CreatePixRequest(
    val KeyType: String,
    val Key: String?,
    contaCliente: ContaCliente
) {
    val bankAccount = BankAccount(
        participant = contaCliente.ispbInstituicao,
        branch = contaCliente.agencia,
        accountNumber = contaCliente.numero,
        accountType = when (contaCliente.tipoConta) {
            CONTA_CORRENTE -> CACC
            CONTA_POUPANCA -> SVGS
        }
    )
    val owner = Owner(
        type = Owner.OwnerType.NATURAL_PERSON,
        name = contaCliente.nomeTitular,
        taxIdNumber = contaCliente.cpfTitular
    )
}

class Owner(
    val type: OwnerType,
    val name: String,
    val taxIdNumber: String
) {
    enum class OwnerType {
        NATURAL_PERSON,
        LEGAL_PERSON
    }
}

class BankAccount(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: AccountType
)