package com.dev0x378.infopo.models

import org.joda.time.DateTime
import java.util.*

open class Item {

    var id: UUID = UUID.randomUUID()
    var name: String = ""
    var ref: String = ""
    var loc: String = ""
    var notes: String = ""
    var version: Int = -1
    var createdAt: DateTime? = DateTime()
    var createdBy: String = "system"
    var updatedAt: DateTime? = DateTime()
    var updatedBy: String = "system"

}