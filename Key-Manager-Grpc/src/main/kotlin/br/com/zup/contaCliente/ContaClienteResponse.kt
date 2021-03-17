package br.com.zup.contaCliente

import br.com.zup.compartilhados.TipoDaConta

data class ContaClienteResponse(
    val tipo: TipoDaConta,
    val instituicao: Instituicao,
    val agencia: String,
    val numero: String,
    val titular: Titular
) {
    fun toModel(): ContaCliente {
        return ContaCliente(
            tipoConta = tipo,
            nomeInstituicao = instituicao.nome,
            ispbInstituicao = instituicao.ispb,
            agencia = agencia,
            numero = numero,
            nomeTitular = titular.nome,
            cpfTitular = titular.cpf
        )
    }
}

class Instituicao(
    val nome: String,
    val ispb: String
)

class Titular(
    val id: String,
    val nome: String,
    val cpf: String
) {
    override fun toString(): String {
        return "Titular(id='$id', nome='$nome', cpf='$cpf')"
    }
}
