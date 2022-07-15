package com.dev0x378.infopo.controllers

import com.tlogx.ktor.pebble.PebbleContent
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

val globalModel = mutableMapOf<String, Any>("title" to "Infopo")

fun Route.default() {

    route("") {

        get("/") {
            call.respond(PebbleContent("index.peb", globalModel))
        }

        get("/about") {
            call.respond(PebbleContent("about.peb", globalModel))
        }
    }
}