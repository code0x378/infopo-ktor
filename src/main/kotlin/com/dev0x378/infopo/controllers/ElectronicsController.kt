package com.dev0x378.infopo.controllers

import com.dev0x378.infopo.models.Electronics
import com.dev0x378.infopo.services.ElectronicsService
import com.tlogx.ktor.pebble.PebbleContent
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import java.util.*

fun Route.electronics(electronicsService: ElectronicsService) {

    route("/electronics") {

        get("/") {
            val model = mutableMapOf<String, Any>("electronics" to electronicsService.getAllElectronics())
            model.putAll(globalModel)
            call.respond(PebbleContent("electronics/list.peb", model))
        }

        post("/search") {
            val postParameters: Parameters = call.receiveParameters()
            val terms = postParameters["search"]
            if (terms.isNullOrBlank()) {
                call.respondRedirect("/electronics")
            } else {
                val model = mutableMapOf<String, Any>(
                        "electronics" to electronicsService.getElectronics(terms),
                        "terms" to terms
                )
                model.putAll(globalModel)
                call.respond(PebbleContent("electronics/list.peb", model))
            }
        }

        get("/create") {
            val model = mutableMapOf<String, Any>("item" to Electronics())
            model.putAll(globalModel)
            call.respond(PebbleContent("electronics/edit.peb", model))
        }

        get("/{id}") {
            val item = electronicsService.getItem(UUID.fromString(call.parameters["id"]))
            if (item == null) call.respond(HttpStatusCode.NotFound)
            else {
                val model = mutableMapOf<String, Any>("item" to item)
                model.putAll(globalModel)
                call.respond(PebbleContent("electronics/edit.peb", model))
            }
        }

        post("/") {
            val postParameters: Parameters = call.receiveParameters()
            val item = electronicsService.paramsToItem(postParameters)
            electronicsService.updateItem(item)
            call.respondRedirect("/electronics")
        }

        get("/{id}/delete") {
            val removed = electronicsService.deleteItem(UUID.fromString(call.parameters["id"]))
            if (removed) call.respondRedirect("/electronics")
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}
