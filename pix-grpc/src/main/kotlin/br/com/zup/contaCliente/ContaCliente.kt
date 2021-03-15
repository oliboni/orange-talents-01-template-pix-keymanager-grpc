package br.com.zup.contaCliente

import br.com.zup.compartilhados.TipoDaConta
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotBlank

@Embeddable
class ContaCliente(
    @field:NotBlank
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val tipoConta: TipoDaConta,
    @field:NotBlank
    @Column(nullable = false)
    val nomeInstituicao: String,
    @field:NotBlank
    @Column(nullable = false)
    val ispbInstituicao: String,
    @field:NotBlank
    @Column(nullable = false)
    val agencia: String,
    @field:NotBlank
    @Column(nullable = false)
    val numero: String,
    @field:NotBlank
    @Column(nullable = false)
    val nomeTitular: String,
    @field:NotBlank
    @Column(nullable = false)
    val cpfTitular: String
)