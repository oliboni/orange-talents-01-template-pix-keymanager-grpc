package br.com.zup.compartilhados

enum class TipoDaConta(tipoConta: String){
    CONTA_CORRENTE("CACC"),
    CONTA_POUPANCA("SVGS");

    fun toBcb(): String {
        return when (this) {
            CONTA_CORRENTE -> "CACC"
            CONTA_POUPANCA -> "SVGS"
        }
    }
}