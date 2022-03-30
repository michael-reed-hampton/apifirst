package name.hampton.mike.apifirst.data

import org.springframework.data.jpa.repository.JpaRepository

/**
 * Set up the simplest possible jpa persistence
 */
interface ThingRepository : JpaRepository<ThingData, Long>