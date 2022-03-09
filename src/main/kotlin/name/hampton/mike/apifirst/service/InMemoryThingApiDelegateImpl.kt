package name.hampton.mike.apifirst.service

import name.hampton.mike.apifirst.util.getLogger
import name.hampton.mike.thing.api.ThingApi
import name.hampton.mike.thing.api.ThingApiDelegate
import name.hampton.mike.thing.model.Thing
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class InMemoryThingApiDelegateImpl : ThingApiDelegate {
    companion object {
        private val loggerWithExplicitClass
                = getLogger(InMemoryThingApiDelegateImpl::class.java)
    }
    // ThingId -> Thing
    val thingMap = mutableMapOf<Long, Thing>()

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
        val filteredMap = thingMap.filterValues { thingIt ->
            // Convert the things tags to strings so we can do an intersection properly
            val stringTags: List<String>? = thingIt.tags?.map { it.name.toString() }
            // If the thing had tags then return true if the two lists intersection is non empty
            // If the thing had no tags, just return false - it does not match
            val hasSome = stringTags?.let {
                val intersection = tags.intersect(it.toSet())
                loggerWithExplicitClass.debug("Thing: ${thingIt.name}, tags: $tags, stringTags: $it, intersection: $intersection")
                intersection.isNotEmpty()
            } ?: false
            hasSome
        }
        return ResponseEntity(filteredMap.values.toList(), HttpStatus.OK)
    }

    /**
     * @see ThingApi#getInventory
     */
    override fun getInventory(): ResponseEntity<Map<String, Int>> {
        val statusToCount = mutableMapOf<String, Int>()
        thingMap.forEach { (_, v: Thing) ->
            val count = statusToCount[v.status.toString()]
            if (null != count){
                statusToCount[v.status.toString()] = count+1
            }
            else {
                statusToCount[v.status.toString()] = 1
            }
        }
        return ResponseEntity.ok(statusToCount)
    }

    /**
     * @see ThingApi#getThingById
     */
    override fun getThingById(thingId: Long): ResponseEntity<Thing> {
        loggerWithExplicitClass.debug("Id: $thingId, Thing: ${thingMap[thingId]}")
        thingMap[thingId]?.let { return ResponseEntity.ok(thingMap[thingId]) }
        loggerWithExplicitClass.debug("Id: $thingId, thingMap: $thingMap")
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    /**
     * @see ThingApi#updateThing
     */
    override fun updateThing(body: Thing): ResponseEntity<Unit> {
        return addThing(body)
    }
}