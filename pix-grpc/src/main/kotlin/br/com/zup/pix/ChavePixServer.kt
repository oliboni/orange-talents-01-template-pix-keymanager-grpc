package br.com.zup.pix

import br.com.zup.RemoveChaveResponse
import br.com.zup.compartilhados.exceptions.AlreadyExistsException
import br.com.zup.compartilhados.exceptions.NotFoundException
import br.com.zup.compartilhados.exceptions.PermissionDeniedException
import br.com.zup.integracao.ClienteIntegracao
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class ChavePixServer(@Inject val serviceIntegracao: ClienteIntegracao,
                     @Inject val chavePixRepository: ChavePixRepository) {

    @Transactional
    fun buscaContaCliente(@Valid request: ChavePixRequest): ChavePix {
        if(chavePixRepository.existsByChave(request.valorChave))
            throw AlreadyExistsException("Chave pix ${request.valorChave} já cadastrada!")

        val response = serviceIntegracao.consultaConta(request.clienteId, request.tipoConta.toString())
        val contaCliente = response.body()?.toModel() ?: throw IllegalStateException("Cliente não encontrado!")

        val chavePix = request.toModel(contaCliente)
        chavePixRepository.save(chavePix)

        return chavePix
    }

    @Transactional
    fun removeChave(@Valid request: RemoveChavePixRequest): RemoveChaveResponse {

        val chavePix = chavePixRepository.findById(UUID.fromString(request.chavePix))
        if (chavePix.isEmpty){
            throw NotFoundException("Chave não encontrada!")
        }

        if(chavePix.get().clienteId != UUID.fromString(request.clienteId)){
            throw PermissionDeniedException("Chave não pertence ao cliente informado!")
        }

        chavePixRepository.delete(chavePix.get())

        return RemoveChaveResponse.newBuilder()
            .setMensagem("Chave ${request.chavePix} excluída com sucesso!")
            .build()
    }
}