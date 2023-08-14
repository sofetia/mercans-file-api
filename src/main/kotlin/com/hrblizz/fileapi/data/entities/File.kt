package com.hrblizz.fileapi.data.entities

import org.springframework.data.annotation.Id
import java.util.Date

class File {
    @Id
    lateinit var uuid: String
    lateinit var name: String
    lateinit var contentType: String
    lateinit var meta: String
    lateinit var source: String
    var expireTime: String? = null
    lateinit var content: ByteArray
    lateinit var createTime: String
}
