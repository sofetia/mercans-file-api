package com.hrblizz.fileapi.data.repository

import com.hrblizz.fileapi.data.entities.File
import com.hrblizz.fileapi.library.log.LogItem
import org.springframework.data.mongodb.repository.MongoRepository

interface LogRepository : MongoRepository<LogItem, String>
