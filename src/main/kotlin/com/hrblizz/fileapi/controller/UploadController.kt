package com.hrblizz.fileapi.controller

import com.hrblizz.fileapi.data.entities.File
import com.hrblizz.fileapi.data.repository.FileRepository
import com.hrblizz.fileapi.rest.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@RestController
class UploadController(
    private val fileRepository: FileRepository
) {
    @RequestMapping("/files/metas", method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllWithIDs(@RequestBody payload: Map<String, List<String>>): ResponseEntity<Map<String, Any>> {
        val list: List<String>? = payload["tokens"];
        if (list.isNullOrEmpty()){

        }
        val ids= fileRepository.findAllById(list);

        for (id in ids){

        }


        return ResponseEntity(
            mapOf(
                "ok" to ids
            ),
            HttpStatus.OK.value()
        )
    }

    @RequestMapping("/files/{id}", method = [RequestMethod.DELETE])
    fun deleteFile(@PathVariable("id") id: String): ResponseEntity<Map<String, Any>> {
        fileRepository.deleteById(id);
        return ResponseEntity(
            mapOf(
                "message" to "file with id $id deleted"
            ),
            HttpStatus.OK.value()
        )
    }

    @RequestMapping("/files/{id}", method = [RequestMethod.GET])
    fun getFile(@PathVariable("id") id: String): Any{
        val optionalFile: Optional<File> = fileRepository.findById(id);
        if (!optionalFile.isPresent){
            return org.springframework.http.ResponseEntity.notFound();
        }

        val file: File = optionalFile.orElse(null)
        val fileData: ByteArray = file.content
        val contentType: MediaType = MediaType.parseMediaType(file.contentType);
        val filename: String = file.name
        val createTime:String = file.createTime

        return org.springframework.http.ResponseEntity.ok()
            .header("X-Filename", filename)
            .header("X-Filesize", fileData.size.toString())
            .header("X-CreateTime", createTime)
            .contentType(contentType)
            .body(fileData);
    }

    @RequestMapping("/files", method = [RequestMethod.POST],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE ])
    fun postFile(@RequestPart("name") name: String,
                 @RequestPart("contentType") contentType: String,
                 @RequestPart("meta") meta: String,
                 @RequestPart("source") source: String,
                 @RequestPart("expireTime") expireTime: String,
                 @RequestPart("content") content: MultipartFile
    ): ResponseEntity<Map<String, Any>> {
        val uuid: String = UUID.randomUUID().toString()
        fileRepository.save(
            File().also {
                it.uuid = uuid
                it.name = name
                it.contentType = contentType
                it.meta = meta
                it.source = source
                it.expireTime = expireTime
                it.content = content.bytes
                val dateTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
                it.createTime = dateTime
            }
        )
        return ResponseEntity(
            mapOf(
                "token" to uuid
            ),
            HttpStatus.CREATED.value()
        )
    }
}