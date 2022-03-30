package name.hampton.mike.apifirst.service

import name.hampton.mike.apifirst.util.getLogger
import name.hampton.mike.thing.api.ThingApi
import name.hampton.mike.thing.api.ThingApiDelegate
import name.hampton.mike.thing.model.Thing
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

// Add tag to allow "Qualifying" this service.
@Service(value="InMemory")
class InMemoryThingApiDelegateImpl : ThingApiDelegate {
    companion object {
        private val loggerWithExplicitClass
                = getLogger(InMemoryThingApiDelegateImpl::class.java)
        @Suppress("SpellCheckingInspection")
        const val PAGESIZE = 100
    }
    // ThingId -> Thing
    val thingMap = mutableMapOf<Int, Thing>()

    /**
     * @see ThingApi#addThing
     */
    override fun addThing(body: Thing): ResponseEntity<Unit> {
        thingMap[body.id]?.let { return ResponseEntity(HttpStatus.CONFLICT) }
        body.id?.let {
            thingMap[body.id] = body
            // todo: This is what we want to return...
            // return ResponseEntity.created(URI)
            return ResponseEntity(HttpStatus.CREATED)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    /**
     * @see ThingApi#deleteThing
     */
    override fun deleteThing(
        thingId: Int,
        apiKey: String?
    ): ResponseEntity<Unit> {
        loggerWithExplicitClass.debug("Id: $thingId, Thing: ${thingMap[thingId]}")
        thingMap.remove(thingId)?.let {
            return ResponseEntity(HttpStatus.NO_CONTENT)
        }
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
            // If the thing had tags then return true if the two lists intersection is nonempty
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
    override fun getThingById(thingId: Int): ResponseEntity<Thing> {
        loggerWithExplicitClass.debug("Id: $thingId, Thing: ${thingMap[thingId]}")
        thingMap[thingId]?.let { return ResponseEntity.ok(it) }
        loggerWithExplicitClass.debug("Id: $thingId, thingMap: $thingMap")
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    /**
     * @see ThingApi#listThings
     */
    override fun listThings(orderBy: String?,
                   pageToken: String?): ResponseEntity<List<Thing>> {
        var from = 0
        pageToken?.let{ try{from = pageToken.toInt()}catch(_:Exception){} }
        // Pagination is actually pretty complicated, especially
        // since there is a sort included here.
        // If the set of data changes between calls to pages,
        // what should be returned?
        // An item could be deleted, which might make items get missed in pages,
        // items could be added, but maybe what the user wanted was the contents
        // at the time of the first request.  If so, then additional
        // filtering should be included, but data may still change
        // while the response is processed.
        // A mostly right way to do this is to hold the initial
        // result set of the initial request, and paginate on that.  This is
        // expensive to memory though.
        //
        // For now be naive, and do offset pagination.  Meaning the
        // passed parameter if effectively the index (first is 0).

        val pageSize = when {
            (thingMap.size >= from + PAGESIZE) -> PAGESIZE
            else -> (thingMap.size - from)
        }
        val theList = thingMap.values.toList()
        val sortedList = when {
            ("createDate" == orderBy) -> theList.sortedBy { it.createDate }
            ("name" == orderBy) -> theList.sortedBy { it.name }
            else -> theList
        }
        return ResponseEntity.ok(sortedList.subList(from, pageSize))
    }

    /**
     * @see ThingApi#updateThing
     */
    override fun updateThing(body: Thing): ResponseEntity<Unit> {
        return addThing(body)
    }
}