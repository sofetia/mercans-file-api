package com.hrblizz.fileapi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.hrblizz.fileapi.controller.exception.BadRequestException
import com.hrblizz.fileapi.data.entities.File
import com.hrblizz.fileapi.data.repository.FileRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import org.springframework.http.ResponseEntity
import com.hrblizz.fileapi.controller.exception.NotFoundException
import com.hrblizz.fileapi.controller.exception.ServiceUnavailableException
import com.hrblizz.fileapi.library.JsonUtil


@RestController
class UploadController(
    private val fileRepository: FileRepository
) {
    @RequestMapping(
        "/files/metas", method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllWithIDs(@RequestBody payload: Map<String, List<String>>): Any? {
        val list: List<String>? = payload["tokens"];
        if (list.isNullOrEmpty()) {
            throw BadRequestException("JSON malformed or in wrong format")
        }

        val ids: Iterable<File> = fileRepository.findAllById(list);

        var files: MutableMap<String, Map<String, Map<String, Any>>> = mutableMapOf();
        var filesInfo: MutableMap<String, Map<String, Any>> = mutableMapOf();
        var fileInfo: MutableMap<String, Any> = mutableMapOf();

        val objectMapper = ObjectMapper()

        for (id: File in ids) {
            fileInfo["token"] = id.uuid
            fileInfo["filename"] = id.name
            fileInfo["size"] = id.content.size.toString()
            fileInfo["contentType"] = id.contentType
            fileInfo["createTime"] = id.createTime
            fileInfo["meta"] = objectMapper.readValue(id.meta, Map::class.java)

            filesInfo[id.uuid] = fileInfo
        }

        files["files"] = filesInfo;

        return JsonUtil.toJson(files);
    }

    @RequestMapping("/files/{id}", method = [RequestMethod.DELETE])
    fun deleteFile(@PathVariable("id") id: String): Any {
        try {
            fileRepository.deleteById(id);
        }
        catch (ex: Exception){
            throw ServiceUnavailableException("Deleting File failed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.value());
    }

    @RequestMapping("/files/{id}", method = [RequestMethod.GET])
    fun getFile(@PathVariable("id") id: String): Any{
        val optionalFile: Optional<File> = fileRepository.findById(id);
        if (!optionalFile.isPresent){
            throw NotFoundException("No file with such token found");
        }

        val file: File = optionalFile.orElse(null)

        val fileData: ByteArray = file.content
        val contentType: MediaType = MediaType.parseMediaType(file.contentType);
        val filename: String = file.name
        val createTime:String = file.createTime

        return ResponseEntity.status(HttpStatus.OK)
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
                 @RequestPart("expireTime", required = false) expireTime: String?,
                 @RequestPart("content") content: MultipartFile
    ): Any {
        val uuid: String = UUID.randomUUID().toString()

        try {
            fileRepository.save(
                File().also {
                    it.uuid = uuid;
                    it.name = name;
                    it.contentType = contentType;
                    it.meta = meta;
                    it.source = source;
                    it.expireTime = expireTime;
                    it.content = content.bytes;
                    val dateTime = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
                    it.createTime = dateTime;
                }
            );
        }
        catch (ex: Exception){
          throw ServiceUnavailableException("Creating file data entry failed");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(mapOf("token" to uuid));
    }
}