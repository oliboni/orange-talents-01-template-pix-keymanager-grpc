package br.com.zup.bcb

import br.com.zup.contaCliente.ContaCliente

class Owner(contaCliente: ContaCliente) {
    val type = "NATURAL_PERSON"
    val name = contaCliente.nomeTitular
    val taxIdNumber = contaCliente.cpfTitular
}