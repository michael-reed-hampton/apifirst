package name.hampton.mike.apifirst.service

import name.hampton.mike.thing.api.ThingApi
import name.hampton.mike.thing.api.ThingApiDelegate
import name.hampton.mike.thing.model.Thing
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class InMemoryThingApiDelegateImpl : ThingApiDelegate {

    // ThingId -> Thing
    val thingMap = mutableMapOf<Long, Thing>()

    // Tag -> [ThingId]
    val taggedThings = mutableMapOf<String, Set<Long>>()

    /**
     * @see ThingApi#addThing
     */
    override fun addThing(body: Thing): ResponseEntity<Unit> {
        body.id?.let {
            thingMap[body.id] = body
            return ResponseEntity.ok(Unit)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    /**
     * @see ThingApi#deleteThing
     */
    override fun deleteThing(
        thingId: Long,
        apiKey: String?
    ): ResponseEntity<Unit> {
        thingMap.remove(thingId)?.let { return ResponseEntity.ok(Unit) }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    /**
     * @see ThingApi#findThingsByStatus
     */
    override fun findThingsByStatus(status: List<String>): ResponseEntity<List<Thing>> {
        val filteredMap = thingMap.filterValues { status.contains(it.status.toString()) }
        return ResponseEntity(filteredMap.values.toList(), HttpStatus.OK)
    }

    /**
     * @see ThingApi#findThingsByTags
     */
    override fun findThingsByTags(tags: List<String>): ResponseEntity<List<Thing>> {
        // Get the sets of things that are tagged - filter by passed tags
        val filteredMap = taggedThings.filter { tags.contains(it.key) }
        // Container for unique thing ids
        val thingIds = mutableSetOf<Long>()
        // Flatten and de-dupe the thing ids.
        filteredMap.forEach { (_, v) -> thingIds.addAll(v) }
        // Filter the things by id
        val thingMapFiltered = thingMap.filterKeys { thingIds.contains(it) }
        // return what w e got
        return ResponseEntity(thingMapFiltered.values.toList(), HttpStatus.OK)
    }

    /**
     * @see ThingApi#getInventory
     */
    override fun getInventory(): ResponseEntity<Map<String, Int>> {
        val statusToCount = mutableMapOf<String, Int>()
        thingMap.forEach { (_, v: Thing) ->
            val count = statusToCount[v.status.toString()]
            if (null != count){ statusToCount[v.status.toString()]?.plus(1) }
            else { statusToCount[v.status.toString()] = 1 }
        }
        return ResponseEntity.ok(statusToCount)
    }

    /**
     * @see ThingApi#getThingById
     */
    override fun getThingById(thingId: Long): ResponseEntity<Thing> {
        thingMap[thingId]?.let { return ResponseEntity.ok(thingMap[thingId]) }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    /**
     * @see ThingApi#updateThing
     */
    override fun updateThing(body: Thing): ResponseEntity<Unit> {
        return addThing(body)
    }
}