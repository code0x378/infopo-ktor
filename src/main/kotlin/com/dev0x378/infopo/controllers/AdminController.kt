package com.dev0x378.infopo.controllers

import com.dev0x378.infopo.models.Apparel
import com.dev0x378.infopo.models.Electronics
import com.dev0x378.infopo.services.ApparelService
import com.dev0x378.infopo.services.ElectronicsService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.tlogx.ktor.pebble.PebbleContent
import io.ktor.application.call
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.request.receiveMultipart
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets

fun Route.admin() {

    val objectMapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    objectMapper.registerModule(JodaModule())
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)

    route("/admin") {

        get("/importexport") {
            call.respond(PebbleContent("admin/importexport.peb", globalModel))
        }

        get("/export/{dataType}") {
            val dataType = call.parameters["dataType"]

            var list = listOf<Any>()

            when (dataType) {
                "apparel" -> {
                    list = ApparelService().getAllApparel()
                }
                "electronics" -> {
                    list = ElectronicsService().getAllElectronics()
                }
                else -> call.respondRedirect("/")
            }
            val content = objectMapper.writeValueAsString(list)
            call.response.header("Content-Disposition", "attachment; filename=\"${dataType}.txt\"")
            call.respond(content.toByteArray(StandardCharsets.UTF_8))
        }

        post("/import/{dataType}") {
            val dataType = call.parameters["dataType"]
            val multipart = call.receiveMultipart()
            var title = ""
            var videoFile: File? = null

            // Processes each part of the multipart input content of the user
            multipart.forEachPart { part ->
                if (part is PartData.FormItem) {
                    if (part.name == "title") {
                        title = part.value
                    }
                } else if (part is PartData.FileItem) {
                    val ext = File(part.originalFileName).extension
                    val file = createTempFile()

                    part.streamProvider().use { its -> file.outputStream().buffered().use { its.copyToSuspend(it) } }
                    videoFile = file
                }

                when (dataType) {


                    "apparel" -> {
                        val list = objectMapper.readValue<List<Apparel>>(videoFile!!)
                        for (apparel: Apparel in list) {
                            ApparelService().addItem(apparel)
                        }
                    }
                    "electronics" -> {
                        val list = objectMapper.readValue<List<Electronics>>(videoFile!!)
                        for (electronics: Electronics in list) {
                            ElectronicsService().addItem(electronics)
                        }
                    }
                    else -> call.respondRedirect("/")
                }

                part.dispose()
                call.respondRedirect("/")
            }


        }
    }
}

suspend fun InputStream.copyToSuspend(
        out: OutputStream,
        bufferSize: Int = DEFAULT_BUFFER_SIZE,
        yieldSize: Int = 4 * 1024 * 1024,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long {
    return withContext(dispatcher) {
        val buffer = ByteArray(bufferSize)
        var bytesCopied = 0L
        var bytesAfterYield = 0L
        while (true) {
            val bytes = read(buffer).takeIf { it >= 0 } ?: break
            out.write(buffer, 0, bytes)
            if (bytesAfterYield >= yieldSize) {
                yield()
                bytesAfterYield %= yieldSize
            }
            bytesCopied += bytes
            bytesAfterYield += bytes
        }
        return@withContext bytesCopied
    }
}
