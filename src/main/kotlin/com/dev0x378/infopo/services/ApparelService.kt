package com.dev0x378.infopo.services

import com.dev0x378.infopo.ServiceHelper.dbQuery
import com.dev0x378.infopo.models.Apparel
import io.ktor.http.Parameters
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import java.util.*

object ApparelTable : Table() {
    val id = uuid("id").primaryKey()
    val name = text("name")
    val ref = text("ref")
    val active = bool("active")
    val notes = text("notes")
    val loc = text("loc")
    val size_xxs = integer("size_xxs")
    val size_xs = integer("size_xs")
    val size_s = integer("size_s")
    val size_m = integer("size_m")
    val size_l = integer("size_l")
    val size_xl = integer("size_xl")
    val version = integer("version")
    val createdAt = datetime("created_at")
    val createdBy = text("created_by")
    val updatedAt = datetime("updated_at")
    val updatedBy = text("updated_by")
}

class ApparelService {

    suspend fun getAllApparel(): List<Apparel> = dbQuery {
        ApparelTable.selectAll().orderBy(ApparelTable.updatedAt to SortOrder.DESC).map { toItem(it) }
    }

    suspend fun getApparel(terms: String): List<Apparel> = dbQuery {
        ApparelTable.select { ApparelTable.name like "%$terms%" or (ApparelTable.notes like "%$terms%") or (ApparelTable.loc like "%$terms%") }.orderBy(ApparelTable.updatedAt to SortOrder.DESC).map { toItem(it) }
    }

    suspend fun getItem(id: UUID): Apparel? = dbQuery {
        ApparelTable.select {
            (ApparelTable.id eq id)
        }.mapNotNull { toItem(it) }
                .singleOrNull()
    }

    suspend fun addItem(apparel: Apparel): Apparel {
        dbQuery {
            ApparelTable.insert {
                it[id] = apparel.id
                it[ref] = apparel.ref
                it[name] = apparel.name
                it[active] = apparel.active
                it[notes] = apparel.notes
                it[loc] = apparel.loc
                it[size_xxs] = apparel.size_xxs
                it[size_xs] = apparel.size_xs
                it[size_s] = apparel.size_s
                it[size_m] = apparel.size_m
                it[size_l] = apparel.size_l
                it[size_xl] = apparel.size_xl
                it[version] = apparel.version + 1
                it[createdAt] = DateTime()
                it[createdBy] = apparel.createdBy
                it[updatedAt] = DateTime()
                it[updatedBy] = apparel.updatedBy
            }
        }
        return getItem(apparel.id)!!
    }

    suspend fun updateItem(apparel: Apparel): Apparel {
        return if (apparel.version == -1) {
            addItem(apparel)
        } else {
            dbQuery {
                ApparelTable.update({ ApparelTable.id eq apparel.id }) {
                    it[id] = apparel.id
                    it[ref] = apparel.ref
                    it[name] = apparel.name
                    it[active] = apparel.active
                    it[notes] = apparel.notes
                    it[loc] = apparel.loc
                    it[size_xxs] = apparel.size_xxs
                    it[size_xs] = apparel.size_xs
                    it[size_s] = apparel.size_s
                    it[size_m] = apparel.size_m
                    it[size_l] = apparel.size_l
                    it[size_xl] = apparel.size_xl
                    it[version] = apparel.version + 1
                    it[createdAt] = DateTime()
                    it[createdBy] = apparel.createdBy
                    it[updatedAt] = DateTime()
                    it[updatedBy] = apparel.updatedBy
                }
            }
            getItem(apparel.id)!!
        }
    }

    suspend fun deleteItem(id: UUID): Boolean = dbQuery {
        ApparelTable.deleteWhere { ApparelTable.id eq id } > 0
    }

    private fun toItem(row: ResultRow): Apparel {
        val apparel = Apparel()
        apparel.id = row[ApparelTable.id]
        apparel.ref = row[ApparelTable.ref]
        apparel.name = row[ApparelTable.name]
        apparel.active = row[ApparelTable.active]
        apparel.notes = row[ApparelTable.notes]
        apparel.loc = row[ApparelTable.loc]
        apparel.size_xxs = row[ApparelTable.size_xxs]
        apparel.size_xs = row[ApparelTable.size_xs]
        apparel.size_s = row[ApparelTable.size_s]
        apparel.size_m = row[ApparelTable.size_m]
        apparel.size_l = row[ApparelTable.size_l]
        apparel.size_xl = row[ApparelTable.size_xl]
        apparel.version = row[ApparelTable.version]
        apparel.createdAt = row[ApparelTable.createdAt]
        apparel.createdBy = row[ApparelTable.createdBy]
        apparel.updatedAt = row[ApparelTable.updatedAt]
        apparel.updatedBy = row[ApparelTable.updatedBy]
        return apparel
    }

    fun paramsToItem(params: Parameters): Apparel {
        val apparel = Apparel()
        apparel.id = if (params["id"] != null && !params["id"].isNullOrBlank()) UUID.fromString(params["id"]) else UUID.randomUUID()
        apparel.ref = params["ref"] ?: ""
        apparel.name = params["name"] ?: ""
        apparel.notes = params["notes"] ?: ""
        apparel.active = params["active"] != null
        apparel.loc = params["loc"] ?: ""
        apparel.size_xxs = params["size_xxs"]?.toInt() ?: 0
        apparel.size_xs = params["size_xs"]?.toInt() ?: 0
        apparel.size_s = params["size_s"]?.toInt() ?: 0
        apparel.size_m = params["size_m"]?.toInt() ?: 0
        apparel.size_l = params["size_l"]?.toInt() ?: 0
        apparel.size_xl = params["size_xl"]?.toInt() ?: 0
        apparel.version = params["version"]?.toInt() ?: 0
        apparel.updatedAt = DateTime()
        return apparel
    }

}
