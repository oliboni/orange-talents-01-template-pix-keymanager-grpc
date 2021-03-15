package br.com.zup.compartilhados.validadores

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UniqueValueValidator::class])
@MustBeDocumented
annotation class UniqueValue(
    val message: String = "Email already exists",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
    val field: String,
    val domainClassName: String
)

@Singleton
class UniqueValueValidator (@PersistenceContext private val manager:EntityManager):
    ConstraintValidator<UniqueValue, String> {

    override fun isValid(
        value: String?,
        annotationMetadata: AnnotationValue<UniqueValue>,
        context: ConstraintValidatorContext
    ): Boolean {
        if(value == null){
            return true
        }
        manager.joinTransaction()
        println(manager.isOpen)
        val query = manager.createQuery("SELECT e FROM ${annotationMetadata.values["domainClassName"]} WHERE ${annotationMetadata.values["field"]}= :value")
            .setParameter("value", value)

//        assert(query.resultList.size > 1) { "Mais de um $domainAttribute com valor $value na tabela $kClassName" }
        manager.close()
        return query.resultList.isEmpty()
    }
}
