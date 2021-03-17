package br.com.zup.pix

import br.com.zup.compartilhados.TipoDaChave
import br.com.zup.contaCliente.ContaCliente
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(
        name = "uk_chave_pix",
        columnNames = ["chave"]
    )]
)
class ChavePix(
    @field:NotBlank
    @Column(nullable = false)
    val clienteId: UUID,

    @field:NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val tipoChave: TipoDaChave,

    @field:NotBlank
    @Column(nullable = false, unique = true)
    val chave: String,

    @field:Valid
    @Embedded
    val contaCliente: ContaCliente,

    @Column(nullable = false)
    val criadaEm: LocalDateTime
) {
    fun pertenceAoCliente(clienteid: UUID): Boolean {
        return clienteId == clienteid
    }

    @Id
    @GeneratedValue
    var id: UUID? = null

    init {
        assert(clienteId != null) { "ClienteId não pode ser nulo" }
        assert(tipoChave != null) { "tipoChave não pode ser nulo" }
        assert(contaCliente != null) { "contaCliente não pode ser nulo" }
    }
}