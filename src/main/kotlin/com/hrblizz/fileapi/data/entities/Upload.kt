package com.hrblizz.fileapi.data.entities

import org.springframework.data.annotation.Id

class Upload {
    @Id
    lateinit var filename: String
}
