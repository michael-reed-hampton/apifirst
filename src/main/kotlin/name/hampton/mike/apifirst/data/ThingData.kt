package name.hampton.mike.apifirst.data

import javax.persistence.*

/**
 * Simple jpa entity to save the data.
 *
 * Next step is to persist the rest of the model in the 'Thing'
 * object.  Especially the 'one to many' data items.
 */
@Table(name = "thing")
@Entity
class ThingData(
    @Column(name = "name", nullable = false)
    var name: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null
    override fun toString(): String {
        return "ThingData(name='$name', id=$id)"
    }
}