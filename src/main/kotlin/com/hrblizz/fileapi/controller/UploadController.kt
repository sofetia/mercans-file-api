package com.hrblizz.fileapi.controller

import com.hrblizz.fileapi.data.entities.File
import com.hrblizz.fileapi.data.repository.UploadRepository
import com.hrblizz.fileapi.rest.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import java.util.UUID

@RestController
class UploadController(
    private val uploadRepository: UploadRepository
) {
    @RequestMapping("/files", method = [RequestMethod.GET])
    fun getUpload(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity(
            mapOf(
                "ok" to true,
                "Message" to "Upload with get"
            ),
            HttpStatus.OK.value()
        )
    }
    @RequestMapping("/files", method = [RequestMethod.POST])
    fun postUpload(@RequestBody file: File): ResponseEntity<Map<String, Any>> {
        val uuid:String = UUID.randomUUID().toString()
        return ResponseEntity(
            mapOf(
                "filename" to file.filename,
                "ok" to true,
                "uuid" to uuid
            ),
            HttpStatus.CREATED.value()
        )
    }
}