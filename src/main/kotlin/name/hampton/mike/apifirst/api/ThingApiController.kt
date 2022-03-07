package name.hampton.mike.apifirst.api

import name.hampton.mike.thing.api.ThingApi
import name.hampton.mike.thing.api.ThingApiDelegate
import org.springframework.web.bind.annotation.RestController

// The annotation below is hugely important.  Without it, the info will not show up on
//  http://localhost:8080/swagger-ui/index.html
@RestController
class ThingApiController(
    // This is auto-configured.
    private val thingApiDelegate: ThingApiDelegate
    ) : ThingApi {

    // Use the auto configured value - which will be name.hampton.mike.apifirst.service.ThingApiDelegateImpl
    override fun getDelegate(): ThingApiDelegate {
        return thingApiDelegate
    }
}