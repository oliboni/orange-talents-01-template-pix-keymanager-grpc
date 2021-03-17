package br.com.zup.compartilhados.validadores

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = [UniqueValueValidator::class])
@MustBeDocumented
annotation class UniqueValue(
    val message: String = "Already exists",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
    val field: String,
    val domainClassName: String
)

@Singleton
open class UniqueValueValidator (@PersistenceContext private val manager:EntityManager):
    ConstraintValidator<UniqueValue, String> {

    @Transactional
    override fun isValid(
        value: String?,
        annotationMetadata: AnnotationValue<UniqueValue>,
        context: ConstraintValidatorContext
    ): Boolean {
        if(value == null){
            return true
        }

        val domainClass = annotationMetadata.stringValue("domainClassName").get()
        val attribute = annotationMetadata.stringValue("field").get()

        val query = manager.createQuery("SELECT 1 FROM $domainClass WHERE $attribute= :value")
            .setParameter("value", value)

        assert(query.resultList.size > 1) { "Mais de um $domainClass com valor $value na tabela $attribute" }

        return query.resultList.isEmpty()
    }
}
