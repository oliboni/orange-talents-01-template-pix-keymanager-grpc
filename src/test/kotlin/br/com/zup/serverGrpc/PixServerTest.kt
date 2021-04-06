package br.com.zup.serverGrpc

import br.com.zup.NovaChaveRequest
import br.com.zup.PixServiceGrpc
import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.bcb.CreatePixKeyResponse
import br.com.zup.bcb.CreatePixRequest
import br.com.zup.compartilhados.TipoDaConta
import br.com.zup.contaCliente.ContaCliente
import br.com.zup.contaCliente.ContaClienteResponse
import br.com.zup.contaCliente.Instituicao
import br.com.zup.contaCliente.Titular
import br.com.zup.integracao.BcbClient
import br.com.zup.integracao.ItauClient
import br.com.zup.pix.ChavePixRepository
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

/**
 * TIP: Necessario desabilitar o controle transacional (transactional=false) pois o gRPC Server
 * roda numa thread separada, caso contrário não será possível preparar cenário dentro do método @Test
 */
@MicronautTest(transactional = false)
internal class PixServerTest(
    val pixRepository: ChavePixRepository,
    val grpcClient: PixServiceGrpc.PixServiceBlockingStub
) {

    @Inject
    lateinit var bcbClient: BcbClient

    @Inject
    lateinit var itauClient: ItauClient

    companion object {
        val CLIENT_ID = UUID.randomUUID()
    }

    @BeforeEach
    fun setup() {
        pixRepository.deleteAll()
    }

    /**
     * Registro
     **/

    @Test
    fun `deve registrar uma nova chave pix`() {
        // cenario
        `when`(itauClient.consultaConta(CLIENT_ID.toString(),TipoConta.CONTA_CORRENTE.name))
            .thenReturn(HttpResponse.ok(buildContaClienteResponse()))

        `when`(bcbClient.registraChave(buildCreatePixKeyRequest()))
            .thenReturn(HttpResponse.created(buildCreatePixKeyResponse()))

        // Acao
        val response = grpcClient.registro(buildNovaChaveRequest(CLIENT_ID))

        // Validacao
        with(response) {
            println(pixId)
            assertEquals(CLIENT_ID.toString(), clienteId)
            assertNotNull(pixId)
        }
    }

    @Test
    fun `nao deve registrar uma chave ja cadastrada`() {
        // Salva chave no banco(cenário)
        pixRepository.save(buildChavePix(CLIENT_ID))

        // Acao
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.registro(buildNovaChaveRequest(CLIENT_ID))
        }

        // Validacao
        with(response) {
            assertEquals(Status.ALREADY_EXISTS.code, response.status.code)
            assertEquals("Chave pix 04585079033 já cadastrada!", response.status.description)
        }
    }

    @Test
    fun `nao deve registrar uma chave quando nao encontra o cliente`() {
        // cenário
        `when`(itauClient.consultaConta(clientId = CLIENT_ID.toString(), tipo = TipoConta.CONTA_CORRENTE.toString()))
            .thenReturn(HttpResponse.notFound())

        // Acao
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.registro(buildNovaChaveRequest(CLIENT_ID))
        }

        // Validacao
        with(response) {
            assertEquals(Status.INVALID_ARGUMENT.code, response.status.code)
            assertEquals("Cliente não encontrado!!", response.status.description)
        }
    }

    @Test
    fun `nao deve registrar chave pix quando parametros forem invalidos`() {
        // ação
        val result = assertThrows<StatusRuntimeException> {
            grpcClient.registro(NovaChaveRequest.newBuilder().build())
        }

        // validação
        with(result) {
            assertEquals(Status.INVALID_ARGUMENT.code, result.status.code)
        }
    }

    /**
     * deletaChave
     **/


    /**
     * consultaChave
     **/

    @Factory
    class client {
        @Bean
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): PixServiceGrpc.PixServiceBlockingStub? {
            return PixServiceGrpc.newBlockingStub(channel)
        }
    }

    @MockBean(BcbClient::class)
    fun bcbClient(): BcbClient? {
        return mock(BcbClient::class.java)
    }

    @MockBean(ItauClient::class)
    fun itauClient(): ItauClient? {
        return mock(ItauClient::class.java)
    }

}