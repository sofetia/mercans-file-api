package com.hrblizz.fileapi.data.entities

import org.springframework.data.annotation.Id

class File {
    @Id
    lateinit var filename: String
}
