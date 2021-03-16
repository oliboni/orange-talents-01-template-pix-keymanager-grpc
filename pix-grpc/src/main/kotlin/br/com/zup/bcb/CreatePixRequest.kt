package br.com.zup.bcb

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
    val bankAccount = BankAccount(contaCliente)
    val owner = Owner(contaCliente)
}

class Owner(contaCliente: ContaCliente) {
    val type = OwnerType.NATURAL_PERSON
    val name = contaCliente.nomeTitular
    val taxIdNumber = contaCliente.cpfTitular

    enum class OwnerType{
        NATURAL_PERSON,
        LEGAL_PERSON
    }
}

class BankAccount(contaCliente: ContaCliente) {
    val participant = contaCliente.ispbInstituicao
    val branch = contaCliente.agencia
    val accountNumber = contaCliente.numero
    val accountType = when (contaCliente.tipoConta) {
        CONTA_CORRENTE -> CACC
        CONTA_POUPANCA -> SVGS
    }
}