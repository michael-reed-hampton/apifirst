package name.hampton.mike.apifirst.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

// https://www.baeldung.com/kotlin/logging
fun getLogger(forClass: Class<*>): Logger =
    LoggerFactory.getLogger(forClass)