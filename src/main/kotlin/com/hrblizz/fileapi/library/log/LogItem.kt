package com.hrblizz.fileapi.library.log

import org.springframework.data.annotation.Id
import java.time.LocalDateTime
import java.util.*

open class LogItem constructor(
    val message: String
) {
    @Id
    val uuid: String = UUID.randomUUID().toString()
    val dateTime: LocalDateTime = LocalDateTime.now()

    var correlationId: String? = null
    var type: String? = null

    override fun toString(): String {
        return "[$dateTime] [$correlationId] $message"
    }
}
