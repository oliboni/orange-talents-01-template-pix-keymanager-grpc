package br.com.zup.bcb

import br.com.zup.contaCliente.ContaCliente

class CreatePixRequest(
    val KeyType: String,
    val Key: String?,
    contaCliente: ContaCliente
) {
    val bankAccount = BankAccount(contaCliente)
    val owner = Owner(contaCliente)
}
