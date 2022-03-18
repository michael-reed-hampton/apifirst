package name.hampton.mike.apifirst

import name.hampton.mike.apifirst.util.getLogger
import name.hampton.mike.thing.api.ThingApiDelegate
import name.hampton.mike.thing.model.Category
import name.hampton.mike.thing.model.Tag
import name.hampton.mike.thing.model.Thing
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

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
    fun doTestLoad() {
        loggerWithExplicitClass.debug("TestDataInitializer is going to load some test data")
        val cat1 = Category(id = 1, name = "Test")
        val tag1 = Tag(id = 1, name = "Tag1")
        val tag2 = Tag(id = 2, name = "Tag2")
        val tag3 = Tag(id = 3, name = "Tag3")
        val tag4 = Tag(id = 4, name = "Tag4")
        val tag5 = Tag(id = 5, name = "Tag5")
        val tag6 = Tag(id = 6, name = "Tag6")

        val base = OffsetDateTime.now()
        getDelegate().addThing(
            Thing(
                name = "Thing1",
                photoUrls = listOf(),
                id = 1,
                category = cat1,
                tags = listOf(tag1, tag2, tag3),
                // random status
                status = Thing.Status.available,
                createDate = base.plusHours(1)
            )
        )
        getDelegate().addThing(
            Thing(
                name = "Thing2",
                photoUrls = listOf(),
                id = 2,
                category = cat1,
                tags = listOf(),
                // random status
                status = Thing.Status.pending,
                createDate = base.plusHours(2)
            )
        )
        getDelegate().addThing(
            Thing(
                name = "Thing3",
                photoUrls = listOf(),
                id = 3,
                category = cat1,
                tags = listOf(tag4, tag5, tag6),
                // random status
                status = Thing.Status.sold,
                createDate = base.plusHours(3)
            )
        )
        getDelegate().addThing(
            Thing(
                name = "Thing4",
                photoUrls = listOf(),
                id = 4,
                category = cat1,
                tags = listOf(tag1, tag2, tag3, tag4, tag5, tag6),
                // random status
                status = Thing.Status.available,
                createDate = base.plusHours(4)
            )
        )
        getDelegate().addThing(
            Thing(
                name = "Thing5",
                photoUrls = listOf(),
                id = 5,
                category = cat1,
                tags = listOf(tag1),
                // random status
                status = Thing.Status.pending,
                createDate = base.plusHours(5)
            )
        )
        getDelegate().addThing(
            Thing(
                name = "0Thing6",
                photoUrls = listOf(),
                id = 6,
                category = cat1,
                tags = listOf(tag1, tag3, tag5),
                // random status
                status = Thing.Status.sold,
                createDate = base.plusHours(6)
            )
        )
    }

//    fun doDemoLoad() {
//        loggerWithExplicitClass.debug("TestDataInitializer is going to load some demo data")
//        val cat1 = Category(id = Random.nextInt(), name = "Test")
//        val tags = mutableListOf<Tag>()
//        for(n in 0..12) {
//            tags.add(Tag(id = Random.nextInt(), name = "Tag$n"))
//        }
//        for(n in 0..12) {
//            val thing = Thing(
//                name = "Thing$n",
//                photoUrls = listOf(),
//                id = Random.nextInt(),
//                category = cat1,
//                // Random size subset of tags.  We shuffle the tags each time, so we get
//                // random tags as well.
//                tags = tags.subList(0, Random.nextInt(tags.size)),
//                // random status
//                status = Thing.Status.values()[Random.nextInt(Thing.Status.values().size)]
//            )
//            tags.shuffle(Random)
//            getDelegate().addThing(thing)
//        }
//    }
}