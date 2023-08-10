package com.hrblizz.fileapi.data.repository

import com.hrblizz.fileapi.data.entities.File
import org.springframework.data.mongodb.repository.MongoRepository

interface UploadRepository : MongoRepository<File, Long>