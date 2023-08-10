package com.hrblizz.fileapi.controller

import com.hrblizz.fileapi.data.entities.Upload
import com.hrblizz.fileapi.data.repository.UploadRepository
import com.hrblizz.fileapi.rest.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.UUID

@RestController
class UploadController(
    private val uploadRepository: UploadRepository
) {
    @RequestMapping("/upload", method = [RequestMethod.GET])
    fun getUpload(): ResponseEntity<Map<String, Any>> {
        val uuid:String = UUID.randomUUID().toString()
        uploadRepository.save(
            Upload().also {
               // it.uuid = uuid
                it.filename = "file.txt"
            }
        )

        return ResponseEntity(
            mapOf(
                "ok" to true,
                "uuid" to uuid
            ),
            HttpStatus.OK.value()
        )
    }
    @RequestMapping("/upload", method = [RequestMethod.POST])
    fun postUpload(@RequestBody upload: Upload): ResponseEntity<Map<String, Any>> {
        val uuid:String = UUID.randomUUID().toString()
        return ResponseEntity(
            mapOf(
                "filename" to upload.filename,
                "ok" to true,
                "uuid" to uuid
            ),
            HttpStatus.OK.value()
        )
    }
}