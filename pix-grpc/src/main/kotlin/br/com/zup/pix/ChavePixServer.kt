package br.com.zup.pix

import br.com.zup.RemoveChaveResponse
import br.com.zup.bcb.CreatePixRequest
import br.com.zup.bcb.DeletePixKeyRequest
import br.com.zup.compartilhados.exceptions.AlreadyExistsException
import br.com.zup.compartilhados.exceptions.NotFoundException
import br.com.zup.compartilhados.exceptions.PermissionDeniedException
import br.com.zup.integracao.ClienteIntegracao
import br.com.zup.integracao.PixKeysIntegracao
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class ChavePixServer(
    @Inject val contaClienteIntegracao: ClienteIntegracao,
    @Inject val pixKeysIntegracao: PixKeysIntegracao,
    @Inject val chavePixRepository: ChavePixRepository
) {
    val log = LoggerFactory.getLogger(ChavePixServer::class.java)

    @Transactional
    fun registraChave(@Valid request: ChavePixRequest): ChavePix {

        log.info("Inciando registro da chave ${request.valorChave}")
        if (chavePixRepository.existsByChave(request.valorChave!!))
            throw AlreadyExistsException("Chave pix ${request.valorChave} já cadastrada!")

        log.info("Buscando conta do cliente ${request.clienteId}")
        val response = contaClienteIntegracao.consultaConta(request.clienteId, request.tipoConta.toString()).body()
        val contaCliente = response?.toModel() ?: throw IllegalStateException("Cliente não encontrado!")

        log.info("Registrando pix no BCB!")
        val retornoBcb = pixKeysIntegracao.registraChave(
            CreatePixRequest(
                request.tipoChave.name,
                request.valorChave,
                contaCliente
            )
        ).let {
            if (it.status != HttpStatus.CREATED) {
                throw IllegalStateException("Erro ao registrar chave PIX no Banco Central do Brasil!")
            }
            it.body()
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
        log.info("Consultando chave ${request.chavePix}")
        val chavePix = chavePixRepository.findById(UUID.fromString(request.chavePix))
        if (chavePix.isEmpty) {
            log.info("Chave não encontrada")
            throw NotFoundException("Chave não encontrada!")
        }

        if (chavePix.get().clienteId != UUID.fromString(request.clienteId)) {
            log.info("Chave não pertence ao cliente")
            throw PermissionDeniedException("Chave não pertence ao cliente informado!")
        }

        DeletePixKeyRequest(
            chavePix.get().chave,
            chavePix.get().contaCliente.ispbInstituicao
        ).let { deleteRequest ->
            pixKeysIntegracao.deletaChave(chavePix.get().chave, deleteRequest).also { deleteResponse ->
                if (deleteResponse.status != HttpStatus.OK) {
                    throw IllegalStateException("Erro ao remover chave PIX do Banco Central do Brasil")
                }
            }
        }

        log.info("Removendo chave interna")
        chavePixRepository.delete(chavePix.get())

        return RemoveChaveResponse.newBuilder()
            .setMensagem("Chave ${request.chavePix} excluída com sucesso!")
            .build()
    }
}