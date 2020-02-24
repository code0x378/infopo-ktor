package com.dev0x378.infopo

import com.dev0x378.infopo.controllers.admin
import com.dev0x378.infopo.controllers.apparel
import com.dev0x378.infopo.controllers.default
import com.dev0x378.infopo.controllers.electronics
import com.dev0x378.infopo.services.ApparelService
import com.dev0x378.infopo.services.ElectronicsService
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.tlogx.ktor.pebble.Pebble
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.routing.route
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import java.io.File
import java.util.*

fun Application.mainModule() {

    val flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost/infopo",
            "infopo", "Uxecfc19741974").load()
//    flyway.clean()
    flyway.migrate()

    initDB()
    install(DefaultHeaders)
    install(ConditionalHeaders)
    install(Compression)
    install(CallLogging)
    install(AutoHeadResponse)
    install(PartialContent)
    install(ContentNegotiation) {
        jackson {
            registerModule(JodaModule())
            enable(SerializationFeature.INDENT_OUTPUT)
//            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
    }
    install(CORS) {
        anyHost()
    }
    install(Pebble) {
        install(Pebble) {
            templateDir = "templates/"
            strictVariables = false
            defaultLocale = Locale.US
            cacheActive = true
            allowGetClass = false
            greedyMatchMethod = false
        }
    }
    val staticfilesDir = File("src/main/resources/static")
    require(staticfilesDir.exists()) { "Cannot find ${staticfilesDir.absolutePath}" }

    install(Routing) {
        default()
        admin()
        electronics(ElectronicsService())
        apparel(ApparelService())
        static {
            route("static") {
                files(staticfilesDir)
            }
        }
    }
}

fun initDB() {
    val config = HikariConfig("/hikari.properties")
    val ds = HikariDataSource(config)
    Database.connect(ds)
    SeedData.load()
}