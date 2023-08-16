package com.hrblizz.fileapi.controller

import com.hrblizz.fileapi.controller.exception.BadRequestException
import com.hrblizz.fileapi.data.repository.FileRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.http.ResponseEntity
import com.hrblizz.fileapi.controller.exception.ServiceUnavailableException
import com.hrblizz.fileapi.library.JsonUtil
import com.hrblizz.fileapi.service.FileService


@RestController
class UploadController(
    private val fileRepository: FileRepository,
    private val fileService: FileService
) {
    @RequestMapping(
        "/files/metas", method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllWithIDs(@RequestBody payload: Map<String, List<String>>): Any? {
        val list: List<String>? = payload["tokens"];
        if (list.isNullOrEmpty()) {
            throw BadRequestException("JSON malformed or in wrong format");
        }
        return JsonUtil.toJson(fileService.getFileInfoAsMap(list));
    }

    @RequestMapping("/files/{id}", method = [RequestMethod.DELETE])
    fun deleteFile(@PathVariable("id") id: String): Any {
        try {
            fileService.delFile(id);
        }
        catch (ex: Exception){
            throw ServiceUnavailableException("Deleting File failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.value());
    }

    @RequestMapping("/files/{id}", method = [RequestMethod.GET])
    fun getFile(@PathVariable("id") id: String): Any{
        return fileService.downloadFile(id);
    }

    @RequestMapping("/files", method = [RequestMethod.POST],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE ])
    fun postFile(@RequestPart("name") name: String,
                 @RequestPart("contentType") contentType: String,
                 @RequestPart("meta") meta: String,
                 @RequestPart("source") source: String,
                 @RequestPart("expireTime", required = false) expireTime: String?,
                 @RequestPart("content") content: MultipartFile
    ): Any {
        val createdUUID = fileService.uploadFile(
            name,
            contentType,
            meta,
            source,
            expireTime,
            content
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapOf("token" to createdUUID));
    }
}
