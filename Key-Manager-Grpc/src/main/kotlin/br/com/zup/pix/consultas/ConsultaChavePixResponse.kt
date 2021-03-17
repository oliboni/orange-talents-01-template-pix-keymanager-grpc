package br.com.zup.pix.consultas

import br.com.zup.ConsultaChaveRespose
import br.com.zup.TipoChave
import br.com.zup.TipoConta
import br.com.zup.compartilhados.TipoDaChave
import br.com.zup.contaCliente.ContaCliente
import br.com.zup.pix.ChavePix
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

data class ConsultaChavePixResponse(
    val chavePixId: UUID?  = null,
    val clienteId: UUID? = null,
    val tipoChave: TipoDaChave,
    val chave: String,
    val contaCliente: ContaCliente,
    val criadoEm: LocalDateTime
){
    companion object{
        fun of(chave: ChavePix): ConsultaChavePixResponse {
            return ConsultaChavePixResponse(
                chavePixId = chave.id,
                clienteId = chave.clienteId,
                tipoChave = chave.tipoChave,
                chave = chave.chave,
                contaCliente = chave.contaCliente,
                criadoEm = chave.criadaEm
            )
        }
    }

    fun converter(): ConsultaChaveRespose {
        return ConsultaChaveRespose.newBuilder()
            .setChavePixId(chavePixId?.toString() ?: "")
            .setClienteId(clienteId?.toString() ?: "")
            .setChavePix(
                ConsultaChaveRespose.ChavePix.newBuilder()
                    .setTipoChave(TipoChave.valueOf(tipoChave.name))
                    .setChave(chave)
                    .setConta(
                        ConsultaChaveRespose.ChavePix.ContaInfo.newBuilder()
                            .setTipoConta(TipoConta.valueOf(contaCliente.tipoConta.name))
                            .setInstituicao(contaCliente.nomeInstituicao)
                            .setNomeTitular(contaCliente.nomeTitular)
                            .setCpfTitular(contaCliente.cpfTitular)
                            .setAgencia(contaCliente.agencia)
                            .setNumber(contaCliente.numero)
                            .build()
                    )
                    .setCriadoEm(
                        criadoEm.atZone(ZoneId.of("UTC")).toInstant().let { instant ->
                            com.google.protobuf.Timestamp.newBuilder()
                                .setNanos(instant.nano)
                                .setSeconds(instant.epochSecond)
                                .build()
                        }
                    )
                    .build()
            )
            .build()
    }
}