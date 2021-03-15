package br.com.zup.compartilhados.validadores

import br.com.zup.pix.ChavePixRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidPixKey(
    val message: String = "Chave Pix inválida!",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class ValidPixKeyValidator: ConstraintValidator<ValidPixKey, ChavePixRequest> {
    private val log = LoggerFactory.getLogger(ValidPixKeyValidator::class.java)
    override fun isValid(
        value: ChavePixRequest?,
        annotationMetadata: AnnotationValue<ValidPixKey>,
        context: io.micronaut.validation.validator.constraints.ConstraintValidatorContext
    ): Boolean {
        log.info("Validando tipo de chave ${value?.tipoChave}")
        if (value?.tipoChave == null) {
            return false
        }

        return value.tipoChave.valida(value.valorChave)
    }
}

