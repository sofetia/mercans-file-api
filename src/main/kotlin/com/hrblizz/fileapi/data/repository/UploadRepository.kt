package com.hrblizz.fileapi.data.repository

import com.hrblizz.fileapi.data.entities.Entity
import com.hrblizz.fileapi.data.entities.Upload
import org.springframework.data.mongodb.repository.MongoRepository

interface UploadRepository : MongoRepository<Upload, Long>