package name.hampton.mike.apifirst.util

import name.hampton.mike.thing.api.ThingApiDelegate
import name.hampton.mike.thing.model.Category
import name.hampton.mike.thing.model.Tag
import name.hampton.mike.thing.model.Thing
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class TestDataInitializer(private val thingApiDelegate: ThingApiDelegate) {
    companion object {
        private val loggerWithExplicitClass
                = getLogger(TestDataInitializer::class.java)
    }

    // Use the auto configured value - which will be name.hampton.mike.apifirst.service.ThingApiDelegateImpl
    fun getDelegate(): ThingApiDelegate {
        return thingApiDelegate
    }

    @EventListener(ApplicationReadyEvent::class)
    fun doSomethingAfterStartup() {
        loggerWithExplicitClass.debug("TestDataInitializer is going to load some testdata")
        val cat1 = Category(id = Random.nextLong(), name = "Test")
        val tags = mutableListOf<Tag>()
        for(n in 0..12) {
            tags.add(Tag(id = Random.nextLong(), name = "Tag$n"))
        }
        for(n in 0..12) {
            val thing = Thing(
                name = "Thing$n",
                photoUrls = listOf(),
                id = Random.nextLong(),
                category = cat1,
                // Random size subset of tags.  We shuffle the tags each time, so we get
                // random tags as well.
                tags = tags.subList(0, Random.nextInt(tags.size)),
                // random status
                status = Thing.Status.values()[Random.nextInt(Thing.Status.values().size)]
            )
            tags.shuffle(Random)
            getDelegate().addThing(thing)
        }
    }
}