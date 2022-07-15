package com.dev0x378.infopo

import com.dev0x378.infopo.services.ApparelTable
import com.dev0x378.infopo.services.ElectronicsTable
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

object SeedData {

    fun load() {
        transaction {
            if (ApparelTable.selectAll().count() == 0) {
                ApparelTable.deleteWhere { ApparelTable.name like "%%" }
                for (i in 1..1000)
                    ApparelTable.insert {
                        it[id] = UUID.randomUUID()
                        it[ref] = "test${i}"
                        it[name] = "test${i}"
                        it[active] = true
                        it[loc] = "Storage ${i}"
                        it[notes] = "Notes ${i}"
                        it[size_xxs] = 1
                        it[size_xs] = 2
                        it[size_s] = 3
                        it[size_m] = 4
                        it[size_l] = 5
                        it[size_xl] = 6
                        it[version] = 0
                        it[createdAt] = DateTime()
                        it[createdBy] = "system"
                        it[updatedAt] = DateTime()
                        it[updatedBy] = "system"
                    }
            }

            if (ElectronicsTable.selectAll().count() == 0) {
                ElectronicsTable.deleteWhere { ElectronicsTable.name like "%%" }
                for (i in 1..1000)
                    ElectronicsTable.insert {
                        it[id] = UUID.randomUUID()
                        it[ref] = "Ref ${i}"
                        it[name] = "Name ${i}"
                        it[loc] = "Location ${i}"
                        it[notes] = "Notes ${i}"
                        it[version] = 0
                        it[createdAt] = DateTime()
                        it[createdBy] = "sa"
                        it[updatedAt] = DateTime()
                        it[updatedBy] = "sa"

                        it[serialNumber] = "SerialNumber ${i}"
                        it[modelNumber] = "Model ${i}"
                        it[status] = 1
                        it[quantity] = 1
                        it[currentValue] = (7.0 * i).toFloat()

                        it[soldBy] = "Ebay"
                        it[soldAt] = null
                        it[soldPrice] = (6*i).toFloat()
                        it[soldShippingPrice] = 9.95f

                        it[manufacturedBy] = "Sony"
                        it[manufacturedAt] = DateTime()
                        it[manufacturedCountry] = "Japan"

                        it[obtainedFrom] = "Goodwill"
                        it[obtainedAt] = DateTime()
                        it[obtainedPrice] = (8*i).toFloat()
                        it[obtainedShippingPrice] = 9.95f
                    }
            }
        }
    }
}
