package com.dev0x378.infopo.services

import com.dev0x378.infopo.ServiceHelper.dbQuery
import com.dev0x378.infopo.models.Electronics
import io.ktor.http.Parameters
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import java.util.*

object ElectronicsTable : Table() {
    val id = uuid("id").primaryKey()
    val name = text("name")
    val ref = text("ref")
    val notes = text("notes")
    val loc = text("loc")
    val version = integer("version")
    val createdAt = datetime("created_at")
    val createdBy = text("created_by")
    val updatedAt = datetime("updated_at")
    val updatedBy = text("updated_by")

    val serialNumber = text("serial_number")
    val modelNumber = text("model_number")
    val status = integer("status")
    val quantity = integer("quantity")
    val currentValue = float("current_value")

    val soldBy = text("sold_by")
    val soldAt = datetime("sold_at").nullable()
    val soldPrice = float("sold_price")
    val soldShippingPrice = float("sold_shipping_price")

    val manufacturedBy = text("manufactured_by")
    val manufacturedAt = datetime("manufactured_at").nullable()
    val manufacturedCountry = text("manufactured_country")

    val obtainedFrom = text("obtained_from")
    val obtainedAt = datetime("obtained_at").nullable()
    val obtainedPrice = float("obtained_price")
    val obtainedShippingPrice = float("obtained_shipping_price")
}

class ElectronicsService {

    suspend fun getAllElectronics(): List<Electronics> = dbQuery {
        ElectronicsTable.selectAll().orderBy(ElectronicsTable.updatedAt to SortOrder.DESC).map { toItem(it) }
    }

    suspend fun getElectronics(terms: String): List<Electronics> = dbQuery {
        ElectronicsTable.select { ElectronicsTable.name like "%$terms%" or (ElectronicsTable.notes like "%$terms%") or (ElectronicsTable.serialNumber like "%$terms%") }.orderBy(ElectronicsTable.updatedAt to SortOrder.DESC).map { toItem(it) }
    }

    suspend fun getItem(id: UUID): Electronics? = dbQuery {
        ElectronicsTable.select {
            (ElectronicsTable.id eq id)
        }.mapNotNull { toItem(it) }
                .singleOrNull()
    }

    suspend fun addItem(electronics: Electronics): Electronics {
        dbQuery {
            ElectronicsTable.insert {
                it[id] = electronics.id
                it[ref] = electronics.ref
                it[name] = electronics.name
                it[loc] = electronics.loc
                it[notes] = electronics.notes
                it[version] = electronics.version + 1
                it[createdAt] = DateTime()
                it[createdBy] = electronics.createdBy
                it[updatedAt] = DateTime()
                it[updatedBy] = electronics.updatedBy

                it[serialNumber] = electronics.serialNumber
                it[modelNumber] = electronics.modelNumber
                it[status] = electronics.status
                it[quantity] = electronics.quantity
                it[currentValue] = electronics.currentValue

                it[soldBy] = electronics.soldBy
                it[soldAt] = electronics.soldAt
                it[soldPrice] = electronics.soldPrice
                it[soldShippingPrice] = electronics.soldShippingPrice

                it[manufacturedBy] = electronics.manufacturedBy
                it[manufacturedAt] = electronics.manufacturedAt
                it[manufacturedCountry] = electronics.manufacturedCountry

                it[obtainedFrom] = electronics.obtainedFrom
                it[obtainedAt] = electronics.obtainedAt
                it[obtainedPrice] = electronics.obtainedPrice
                it[obtainedShippingPrice] = electronics.obtainedShippingPrice

            }
        }
        return getItem(electronics.id)!!
    }

    suspend fun updateItem(electronics: Electronics): Electronics {
        return if (electronics.version == -1) {
            addItem(electronics)
        } else {
            dbQuery {
                ElectronicsTable.update({ ElectronicsTable.id eq electronics.id }) {
                    it[id] = electronics.id
                    it[ref] = electronics.ref
                    it[name] = electronics.name
                    it[loc] = electronics.loc
                    it[notes] = electronics.notes
                    it[version] = electronics.version + 1
                    it[createdAt] = DateTime()
                    it[createdBy] = electronics.createdBy
                    it[updatedAt] = DateTime()
                    it[updatedBy] = electronics.updatedBy

                    it[serialNumber] = electronics.serialNumber
                    it[modelNumber] = electronics.modelNumber
                    it[status] = electronics.status
                    it[quantity] = electronics.quantity
                    it[currentValue] = electronics.currentValue

                    it[soldBy] = electronics.soldBy
                    it[soldAt] = electronics.soldAt
                    it[soldPrice] = electronics.soldPrice
                    it[soldShippingPrice] = electronics.soldShippingPrice

                    it[manufacturedBy] = electronics.manufacturedBy
                    it[manufacturedAt] = electronics.manufacturedAt
                    it[manufacturedCountry] = electronics.manufacturedCountry

                    it[obtainedFrom] = electronics.obtainedFrom
                    it[obtainedAt] = electronics.obtainedAt
                    it[obtainedPrice] = electronics.obtainedPrice
                    it[obtainedShippingPrice] = electronics.obtainedShippingPrice
                }
            }
            getItem(electronics.id)!!
        }
    }

    suspend fun deleteItem(id: UUID): Boolean = dbQuery {
        ElectronicsTable.deleteWhere { ElectronicsTable.id eq id } > 0
    }

    private fun toItem(row: ResultRow): Electronics {
        val electronics = Electronics()
        electronics.id = row[ElectronicsTable.id]
        electronics.ref = row[ElectronicsTable.ref]
        electronics.name = row[ElectronicsTable.name]
        electronics.loc = row[ElectronicsTable.loc]
        electronics.notes = row[ElectronicsTable.notes]
        electronics.version = row[ElectronicsTable.version]
        electronics.createdAt = row[ElectronicsTable.createdAt]
        electronics.createdBy = row[ElectronicsTable.createdBy]
        electronics.updatedAt = row[ElectronicsTable.updatedAt]
        electronics.updatedBy = row[ElectronicsTable.updatedBy]

        electronics.serialNumber = row[ElectronicsTable.serialNumber]
        electronics.modelNumber = row[ElectronicsTable.modelNumber]
        electronics.status = row[ElectronicsTable.status]
        electronics.quantity = row[ElectronicsTable.quantity]
        electronics.currentValue = row[ElectronicsTable.currentValue]

        electronics.soldBy = row[ElectronicsTable.soldBy]
        electronics.soldAt = row[ElectronicsTable.soldAt]
        electronics.soldPrice = row[ElectronicsTable.soldPrice]
        electronics.soldShippingPrice = row[ElectronicsTable.soldShippingPrice]

        electronics.manufacturedBy = row[ElectronicsTable.manufacturedBy]
        electronics.manufacturedAt = row[ElectronicsTable.manufacturedAt]
        electronics.manufacturedCountry = row[ElectronicsTable.manufacturedCountry]

        electronics.obtainedFrom = row[ElectronicsTable.obtainedFrom]
        electronics.obtainedAt = row[ElectronicsTable.obtainedAt]
        electronics.obtainedPrice = row[ElectronicsTable.obtainedPrice]
        electronics.obtainedShippingPrice = row[ElectronicsTable.obtainedShippingPrice]

        return electronics
    }

    fun paramsToItem(params: Parameters): Electronics {
        val electronics = Electronics()
        electronics.id = if (params["id"] != null && !params["id"].isNullOrBlank()) UUID.fromString(params["id"]) else UUID.randomUUID()
        electronics.ref = params["ref"] ?: ""
        electronics.name = params["name"] ?: ""
        electronics.notes = params["notes"] ?: ""
        electronics.loc = params["loc"] ?: ""
        electronics.updatedAt = DateTime()
        electronics.version = params["version"]?.toInt() ?: 0

        electronics.serialNumber = params["serialNumber"] ?: ""
        electronics.modelNumber = params["modelNumber"] ?: ""
        electronics.status = params["status"]?.toInt() ?: 0
        electronics.quantity = params["quantity"]?.toInt() ?: 0
        electronics.currentValue = params["currentValue"]?.toFloat() ?: 0f

        electronics.soldBy = params["soldBy"] ?: ""
        electronics.soldAt =  if (params["soldAt"] != null && !params["soldAt"].isNullOrBlank()) DateTime(params["soldAt"]) else null
        electronics.soldPrice = params["soldPrice"]?.toFloat() ?: 0f
        electronics.soldShippingPrice = params["soldShippingPrice"]?.toFloat() ?: 0f

        electronics.manufacturedBy = params["manufacturedBy"] ?: ""
        electronics.manufacturedAt =  if (params["manufacturedAt"] != null && !params["manufacturedAt"].isNullOrBlank()) DateTime(params["manufacturedAt"]) else null
        electronics.manufacturedCountry = params["manufacturedCountry"] ?: ""

        electronics.obtainedFrom = params["obtainedFrom"] ?: ""
        electronics.obtainedAt = if (params["obtainedAt"] != null && !params["obtainedAt"].isNullOrBlank()) DateTime(params["obtainedAt"]) else null
        electronics.obtainedPrice = params["obtainedPrice"]?.toFloat() ?: 0f
        electronics.obtainedShippingPrice = params["obtainedShippingPrice"]?.toFloat() ?: 0f

        return electronics
    }

}
