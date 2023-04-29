package space.jay.wirebarley.core.common.delay

import java.util.UUID

internal data class StateDelayMessage<T>(
    val isDirect : Boolean = true,
    val message : T? = null,
    val id : Long = UUID.randomUUID().mostSignificantBits
)
