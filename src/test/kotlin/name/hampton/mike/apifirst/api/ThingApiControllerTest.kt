package name.hampton.mike.apifirst.api

import com.fasterxml.jackson.databind.ObjectMapper
import name.hampton.mike.thing.model.Category
import name.hampton.mike.thing.model.Thing
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

// See name.hampton.mike.apifirst.TestDataInitializer.doTestLoad for expected values

@SpringBootTest
@AutoConfigureMockMvc
internal class ThingApiControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    val baseUrl = "/v1/thing"

    @Nested
    @DisplayName("GET /v1/thing/{thingId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetExistingThing {
        @Test
        // @DirtiesContext
        fun `should get the thing with the given id`() {
            // given
            val id = 1
            // Make sure it is there
            mockMvc.get("$baseUrl/$id")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.id") {
                        value("1")
                    }
                }
        }
    }

    @Nested
    @DisplayName("POST /v1/thing/{thingId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class AddThings {
        @Test
        @DirtiesContext
        fun `should add the non-existing thing with the given id`() {
            // given
            val thing7 = Thing(
                name = "Thing7",
                photoUrls = listOf(),
                id = 7,
                category = Category(id = 2, name = "Live Test"),
                tags = listOf(),
                status = Thing.Status.pending
            )

            // Make sure it is NOT there
            mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(thing7)
            }
                .andDo { print() }
                .andExpect {
                    status {  isCreated() }
                    // eventually, would like to have a uri pointing to the location
                    // (XXX/thing/7) returned here in some way
                    // https://datatracker.ietf.org/doc/html/rfc7231#section-6.3.2
                    // The 201 response payload typically describes and links to the
                    // resource(s) created.
                }
        }

        @Test
        @DirtiesContext
        fun `should not add the existing thing with the given id`() {
            val thing1 = Thing(
                name = "Thing1",
                photoUrls = listOf(),
                id = 1,
                category = Category(id = 2, name = "Live Test"),
                tags = listOf(),
                status = Thing.Status.pending
            )

            // Make sure it is NOT there
            mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(thing1)
            }
                .andDo { print() }
                .andExpect {
                    status {  isConflict() }
                    // eventually, would like to have a uri pointing to the location
                    // of the existing (XXX/thing/1) returned here in some way
                }
        }
    }

    @Nested
    @DisplayName("DELETE /v1/thing/{thingId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteExistingThing {
        @Test
        @DirtiesContext
        fun `should delete the thing with the given id`() {
            // given
            val id = 1
            mockMvc.get("$baseUrl/$id")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.id") {
                        value("1")
                    }
                }
            // Delete it
            mockMvc.delete("$baseUrl/$id")
                .andDo { print() }
                .andExpect { status { isNoContent() } }
            // Make sure it is no longer there
            mockMvc.get("$baseUrl/$id")
                .andExpect { status { isNotFound() } }
        }

        @Test
        fun `should return Bad Request the id is non numeric`() {
            // given
            val invalidId = "non_numeric"
            // when/then
            mockMvc.delete("$baseUrl/$invalidId")
                .andDo { print() }
                .andExpect { status { isBadRequest() } }
        }

        @Test
        fun `should return NOT FOUND if no thing with given account number exists`() {
            // given
            val invalidId = "1000"
            // when/then
            mockMvc.delete("$baseUrl/$invalidId")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }
}