package name.hampton.mike.apifirst.service

import name.hampton.mike.thing.api.ThingApi
import name.hampton.mike.thing.api.ThingApiDelegate
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ThingApiDelegateImpl : ThingApiDelegate {

    /**
     * @see ThingApi#getInventory
     */
    override fun getInventory(): ResponseEntity<Map<String, Int>> {
        // return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
        val inventory = mutableMapOf<String, Int>()
        inventory["foo"] = 1
        return ResponseEntity.ok(inventory)
    }
}