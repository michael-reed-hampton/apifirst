package name.hampton.mike.apifirst.service

import name.hampton.mike.apifirst.data.ThingData
import name.hampton.mike.apifirst.data.ThingRepository
import name.hampton.mike.apifirst.util.getLogger
import name.hampton.mike.thing.model.Thing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

/**
 * A very simple example of actually persisting data using jpa
 */
// Add tag to allow "Qualifying" this service.
@Service(value="Persisted")
class PersistedThingApiDelegateImpl(
    @Autowired val thingRepository: ThingRepository) : InMemoryThingApiDelegateImpl() {
        companion object {
            private val loggerWithExplicitClass
                    = getLogger(PersistedThingApiDelegateImpl::class.java)
        }

    override fun addThing(body: Thing): ResponseEntity<Unit> {
        val superAdd = super.addThing(body)
        // now persist it.
        val thingData = ThingData(name = body.name)
        thingRepository.save(thingData)

        loggerWithExplicitClass.debug("The things are ${thingRepository.findAll()}")

        return superAdd
    }
}