package com.hrblizz.fileapi.controller.exception

import com.hrblizz.fileapi.library.log.Logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
class ServiceUnavailableException(
    message: String
) : BadRequestException(message){}
