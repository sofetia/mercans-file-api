package com.hrblizz.fileapi.library.log

import com.hrblizz.fileapi.data.repository.LogRepository
import org.springframework.stereotype.Component

@Component
class Logger (
    private val logRepository: LogRepository
) {
    fun info(logItem: LogItem) {
        write("info", logItem)
    }

    fun warning(logItem: LogItem) {
        write("warning", logItem)
    }

    fun error(logItem: LogItem) {
        write("error", logItem)
    }

    fun crit(logItem: LogItem) {
        write("crit", logItem)
    }

    private fun write(logLevel: String, logItem: LogItem) {
        println("$logLevel: $logItem")
        logRepository.save(
            LogItem("$logLevel: $logItem")
        )
    }
}
