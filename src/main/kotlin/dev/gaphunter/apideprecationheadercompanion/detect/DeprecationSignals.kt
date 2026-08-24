package dev.gaphunter.apideprecationheadercompanion.detect

/**
 * Text signals this plugin uses -- deliberately broad substring matches
 * (not resolved calls), same "match a known name, don't resolve a
 * symbol" discipline as `SqlSignalNames`/`CorrelationSignals` elsewhere
 * in this catalog.
 */
object DeprecationSignals {

    /** Mapping annotations that mark a Spring MVC endpoint. */
    val MAPPING_ANNOTATIONS = setOf("GetMapping", "PostMapping", "PutMapping", "DeleteMapping", "PatchMapping", "RequestMapping")

    /**
     * Signals that a method body sets a deprecation-related HTTP
     * response header -- RFC 9745 "Deprecation", RFC 8594 "Sunset", or
     * the common non-standard "X-Deprecated" some APIs use instead.
     */
    private val HEADER_FRAGMENTS = listOf(
        "\"deprecation\"",
        "\"sunset\"",
        "\"x-deprecated\"",
        "header(\"deprecation\"",
        "header(\"sunset\"",
    )

    fun bodySetsDeprecationHeader(bodyText: String): Boolean {
        val lower = bodyText.lowercase()
        return HEADER_FRAGMENTS.any { lower.contains(it) }
    }
}
