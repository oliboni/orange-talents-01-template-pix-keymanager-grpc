package br.com.zup.pix.consultas

import br.com.zup.compartilhados.exceptions.NotFoundException
import br.com.zup.compartilhados.validadores.ValidUUID
import br.com.zup.integracao.BcbClient
import br.com.zup.pix.ChavePixRepository
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpStatus
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


@Introspected
sealed class ConsultaChave{

    abstract fun filtra(chavePixRepository: ChavePixRepository, bcbClient: BcbClient): ConsultaChavePixResponse

    @Introspected
    data class PorChavePixId(
        @field:NotBlank @field:ValidUUID val chavePixid: String,
        @field:NotBlank @field:ValidUUID val clienteId: String
    ) : ConsultaChave() {
        override fun filtra(chavePixRepository: ChavePixRepository, bcbClient: BcbClient): ConsultaChavePixResponse {
            return chavePixRepository
                .findById(UUID.fromString(chavePixid))
                .filter { it.pertenceAoCliente(UUID.fromString(clienteId)) }
                .map(ConsultaChavePixResponse.Companion::of)
                .orElseThrow { NotFoundException("Chave não encontrada") }
        }
    }

    @Introspected
    data class PorChave(@field:Size(max = 77) val chave: String) : ConsultaChave() {
        private val log = LoggerFactory.getLogger(ConsultaChave::class.java)

        override fun filtra(chavePixRepository: ChavePixRepository, bcbClient: BcbClient): ConsultaChavePixResponse {
            return chavePixRepository
                .findByChave(chave)
                .map(ConsultaChavePixResponse.Companion::of)
                .orElseGet {
                    log.info("Consultando chave $chave no Banco Central do Brasil")
                    bcbClient.consultaChave(chave).let { httpResponse ->
                        if (httpResponse.status != HttpStatus.OK){
                            throw NotFoundException("Chave pix não encontrada")
                        }
                        httpResponse.body()?.toModel()
                    }
                }
        }


    }

    @Introspected
    class Invalida : ConsultaChave() {
        override fun filtra(chavePixRepository: ChavePixRepository, bcbClient: BcbClient): ConsultaChavePixResponse {
            throw IllegalArgumentException("Consulta inválida")
        }

    }
}