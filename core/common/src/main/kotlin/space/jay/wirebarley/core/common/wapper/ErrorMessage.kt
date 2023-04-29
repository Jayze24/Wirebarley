package space.jay.wirebarley.core.common.wapper

import java.util.*

data class ErrorMessage(
    val id : Long = UUID.randomUUID().mostSignificantBits,
    val message : String
)
