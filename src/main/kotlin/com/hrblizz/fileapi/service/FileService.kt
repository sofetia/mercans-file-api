package com.hrblizz.fileapi.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.hrblizz.fileapi.controller.exception.NotFoundException
import com.hrblizz.fileapi.controller.exception.ServiceUnavailableException
import com.hrblizz.fileapi.data.entities.File
import com.hrblizz.fileapi.data.repository.FileRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class FileService(
    private val fileRepository: FileRepository
) {
    fun getFileInfoAsMap(list: List<String>):
            MutableMap<String, Map<String, Map<String, Any>>>
    {
        val ids: Iterable<File> = fileRepository.findAllById(list);

        val files: MutableMap<String, Map<String, Map<String, Any>>> = mutableMapOf();
        val filesInfo: MutableMap<String, Map<String, Any>> = mutableMapOf();
        val fileInfo: MutableMap<String, Any> = mutableMapOf();

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

        return files;
    }

    fun delFile(id: String){
        fileRepository.deleteById(id);
    }

    fun uploadFile(name: String, contentType: String, meta: String,
                   source: String, expireTime: String?, content: MultipartFile): Any{
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
        return uuid;
    }

    fun downloadFile(id: String): Any{
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
}
