package br.com.zup.bcb

import br.com.zup.contaCliente.ContaCliente

class BankAccount(contaCliente: ContaCliente) {
    val participant = contaCliente.ispbInstituicao
    val branch = contaCliente.agencia
    val accountNumber = contaCliente.numero
    val accountType = contaCliente.tipoConta.toBcb()
}