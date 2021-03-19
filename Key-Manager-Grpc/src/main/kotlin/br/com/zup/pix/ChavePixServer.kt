package br.com.zup.pix

import br.com.zup.ConsultaByClienteResponse
import br.com.zup.RemoveChaveResponse
import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.bcb.CreatePixKeyResponse
import br.com.zup.bcb.CreatePixRequest
import br.com.zup.bcb.DeletePixKeyRequest
import br.com.zup.compartilhados.exceptions.AlreadyExistsException
import br.com.zup.compartilhados.exceptions.NotFoundException
import br.com.zup.compartilhados.exceptions.PermissionDeniedException
import br.com.zup.integracao.ItauClient
import br.com.zup.integracao.BcbClient
import br.com.zup.pix.consultas.ConsultaChaveByClienteRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class ChavePixServer(
    @Inject val contaItauClient: ItauClient,
    @Inject val bcbClient: BcbClient,
    @Inject val chavePixRepository: ChavePixRepository
) {
    val log = LoggerFactory.getLogger(ChavePixServer::class.java)

    @Transactional
    fun registraChave(@Valid request: ChavePixRequest): ChavePix {

        log.info("Inciando registro da chave ${request.valorChave}")
        if (chavePixRepository.existsByChave(request.valorChave!!))
            throw AlreadyExistsException("Chave pix ${request.valorChave} já cadastrada!")

        log.info("Buscando conta do cliente ${request.clienteId}")
        val response = contaItauClient.consultaConta(request.clienteId!!, request.tipoConta.toString())
            .let {
                if (it.status != HttpStatus.OK) {
                    throw IllegalArgumentException("Cliente não encontrado!!")
                }
                it.body()
            }
        val contaCliente = response?.toModel() ?: throw IllegalStateException("Cliente não encontrado!")

        log.info("Registrando pix no BCB!")
        var retornoBcb: CreatePixKeyResponse?
        try {
            retornoBcb = bcbClient.registraChave(
                CreatePixRequest(
                    request.tipoChave!!.name,
                    request.valorChave,
                    contaCliente
                )
            ).body()
        } catch (e : HttpClientResponseException){
            throw IllegalStateException("Erro ao registrar chave PIX no Banco Central do Brasil!")
        }


        val chavePix = retornoBcb!!.toModel(
            UUID.fromString(request.clienteId),
            contaCliente
        )

        log.info("Salvando chave ${chavePix.chave}")
        chavePixRepository.save(chavePix)

        return chavePix
    }

    @Transactional
    fun removeChave(@Valid request: RemoveChavePixRequest): RemoveChaveResponse {
        log.info("Consultando chave ${request.chavePixId}")
        val chavePix = chavePixRepository.findById(UUID.fromString(request.chavePixId))
        if (chavePix.isEmpty) {
            log.info("Chave não encontrada")
            throw NotFoundException("Chave não encontrada!")
        }

        if (!chavePix.get().pertenceAoCliente(UUID.fromString(request.clienteId))) {
            log.info("Chave não pertence ao cliente")
            throw PermissionDeniedException("Chave não pertence ao cliente informado!")
        }

        DeletePixKeyRequest(
            chavePix.get().chave,
            chavePix.get().contaCliente.ispbInstituicao
        ).let { deleteRequest ->
            bcbClient.deletaChave(chavePix.get().chave, deleteRequest).also { deleteResponse ->
                if (deleteResponse.status != HttpStatus.OK) {
                    throw IllegalStateException("Erro ao remover chave PIX do Banco Central do Brasil")
                }
            }
        }

        log.info("Removendo chave interna")
        chavePixRepository.delete(chavePix.get())

        return RemoveChaveResponse.newBuilder()
            .setMensagem("Chave ${request.chavePixId} excluída com sucesso!")
            .build()
    }

    @Transactional
    fun consultaChaveCliente(@Valid request: ConsultaChaveByClienteRequest): ConsultaByClienteResponse {
        log.info("Buscando chaves clienteId ${request.clienteId}")

        val listaChaves = chavePixRepository.findByClienteId(UUID.fromString(request.clienteId)).map {
            ConsultaByClienteResponse.ChavePix.newBuilder()
                .setPixId(it.id.toString())
                .setTipoChave(TipoChave.valueOf(it.tipoChave.name))
                .setChave(it.chave)
                .setTipoConta(TipoConta.valueOf(it.contaCliente.tipoConta.name))
                .setCriadoEm(
                    it.criadaEm.atZone(ZoneId.of("UTC"))
                        .toInstant().let { instant ->
                            com.google.protobuf.Timestamp.newBuilder()
                                .setNanos(instant.nano)
                                .setSeconds(instant.epochSecond)
                                .build()
                        })
                .build()
        }

        return ConsultaByClienteResponse.newBuilder()
            .setClienteId(request.clienteId)
            .addAllChavePix(listaChaves)
            .build()
    }
}