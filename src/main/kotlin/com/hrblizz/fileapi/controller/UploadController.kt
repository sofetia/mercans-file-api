package com.hrblizz.fileapi.controller

import com.hrblizz.fileapi.data.entities.Entity
import com.hrblizz.fileapi.data.entities.File
import com.hrblizz.fileapi.data.repository.FileRepository
import com.hrblizz.fileapi.rest.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
class UploadController(
    private val fileRepository: FileRepository
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
    @RequestMapping("/files", method = [RequestMethod.POST],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE ])
    fun postFile(@RequestPart file: File, @RequestPart document: MultipartFile): ResponseEntity<Map<String, Any>> {
        val uuid:String = UUID.randomUUID().toString()
        fileRepository.save(
            File().also {
                it.name = UUID.randomUUID().toString()
                it.document = document
            }
        )
        return ResponseEntity(
            mapOf(
                //"filename" to file,
                "ok" to true,
                "uuid" to uuid
            ),
            HttpStatus.CREATED.value()
        )
    }
}