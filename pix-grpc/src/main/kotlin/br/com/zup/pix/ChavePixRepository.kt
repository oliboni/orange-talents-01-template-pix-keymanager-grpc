package br.com.zup.pix

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChavePixRepository:JpaRepository<ChavePix,UUID> {
    fun existsByChave(valorChave: String): Boolean
}