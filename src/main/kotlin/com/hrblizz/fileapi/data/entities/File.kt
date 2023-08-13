package com.hrblizz.fileapi.data.entities

import org.springframework.data.annotation.Id
import org.springframework.web.multipart.MultipartFile

class File {
    @Id
    lateinit var name: String
    var document: MultipartFile? = null
}
