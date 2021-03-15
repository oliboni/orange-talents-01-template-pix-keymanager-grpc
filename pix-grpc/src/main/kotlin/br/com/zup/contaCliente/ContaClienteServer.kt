package br.com.zup.contaCliente

import br.com.zup.compartilhados.exceptions.AlreadyExistsException
import br.com.zup.integracao.ClienteIntegracao
import br.com.zup.pix.ChavePix
import br.com.zup.pix.ChavePixRepository
import br.com.zup.pix.ChavePixRequest
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class ContaClienteServer(@Inject val serviceIntegracao: ClienteIntegracao,
                         @Inject val chavePixRepository: ChavePixRepository) {

    @Transactional
    fun buscaContaCliente(@Valid request: ChavePixRequest): ChavePix {
        if(chavePixRepository.existsByChave(request.valorChave!!))
            throw AlreadyExistsException("Chave pix ${request.valorChave} já cadastrada!")

        val response = serviceIntegracao.consultaConta(request.clienteId!!, request.tipoConta.toString())
        val contaCliente = response.body()?.toModel() ?: throw IllegalStateException("Cliente não encontrado!")

        val chavePix = request.toModel(contaCliente)
        chavePixRepository.save(chavePix)

        return chavePix
    }
}