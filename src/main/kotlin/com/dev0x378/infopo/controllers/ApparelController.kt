package com.dev0x378.infopo.controllers

import com.dev0x378.infopo.models.Apparel
import com.dev0x378.infopo.services.ApparelService
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

fun Route.apparel(apparelService: ApparelService) {

    route("/apparel") {

        get("/") {
            val model = mutableMapOf<String, Any>("apparel" to apparelService.getAllApparel())
            model.putAll(globalModel)
            call.respond(PebbleContent("apparel/list.peb", model))
        }

        post("/search") {
            val postParameters: Parameters = call.receiveParameters()
            val terms = postParameters["search"]
            if (terms.isNullOrBlank()) {
                call.respondRedirect("/apparel")
            } else {
                val model = mutableMapOf<String, Any>(
                        "apparel" to apparelService.getApparel(terms),
                        "terms" to terms
                )
                model.putAll(globalModel)
                call.respond(PebbleContent("apparel/list.peb", model))
            }
        }

        get("/create") {
            val model = mutableMapOf<String, Any>("item" to Apparel())
            model.putAll(globalModel)
            call.respond(PebbleContent("apparel/edit.peb", model))
        }

        get("/{id}") {
            val item = apparelService.getItem(UUID.fromString(call.parameters["id"]))
            if (item == null) call.respond(HttpStatusCode.NotFound)
            else {
                val model = mutableMapOf<String, Any>("item" to item)
                model.putAll(globalModel)
                call.respond(PebbleContent("apparel/edit.peb", model))
            }
        }

        post("/") {
            val postParameters: Parameters = call.receiveParameters()
            val item = apparelService.paramsToItem(postParameters)
            apparelService.updateItem(item)
            call.respondRedirect("/apparel")
        }

        get("/{id}/delete") {
            val removed = apparelService.deleteItem(UUID.fromString(call.parameters["id"]))
            if (removed) call.respondRedirect("/apparel")
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}
