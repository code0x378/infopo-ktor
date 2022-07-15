package com.dev0x378.infopo.models

import org.joda.time.DateTime

class Electronics : Item() {

    var serialNumber: String = ""
    var modelNumber: String = ""
    var status: Int = 0
    var quantity: Int = 0
    var currentValue: Float = 0f

    var soldBy: String = ""
    var soldAt: DateTime? = null
    var soldPrice: Float = 0f
    var soldShippingPrice: Float = 0f

    var manufacturedBy: String = ""
    var manufacturedAt: DateTime? = null
    var manufacturedCountry: String = ""

    var obtainedFrom: String = ""
    var obtainedAt: DateTime? = null
    var obtainedPrice: Float = 0f
    var obtainedShippingPrice: Float = 0f

}