package com.kt.kt_web.tasks

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

fun postgres(imageName: String, opts: JdbcDatabaseContainer<Nothing>.() -> Unit) =
    PostgreSQLContainer<Nothing>(DockerImageName.parse(imageName)).apply(opts)

fun pgAdmin(imageName: String, opts: GenericContainer<Nothing>.() -> Unit) =
    GenericContainer<Nothing>(DockerImageName.parse(imageName)).apply(opts)