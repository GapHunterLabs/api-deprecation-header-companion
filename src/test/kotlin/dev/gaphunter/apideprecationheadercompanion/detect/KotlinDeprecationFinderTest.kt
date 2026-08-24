package dev.gaphunter.apideprecationheadercompanion.detect

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class KotlinDeprecationFinderTest : BasePlatformTestCase() {

    fun `test a deprecated endpoint with no header signal is flagged`() {
        val file = myFixture.configureByText(
            "OrderController.kt",
            """
            class OrderController {
                @Deprecated("use v2")
                @GetMapping("/orders/legacy")
                fun legacyOrders(): String {
                    return orderService.fetchAll()
                }
            }
            """.trimIndent(),
        )
        assertEquals(1, KotlinDeprecationFinder.findAll(file).size)
    }

    fun `test a deprecated endpoint setting the Sunset header is not flagged`() {
        val file = myFixture.configureByText(
            "OrderController.kt",
            """
            class OrderController {
                @Deprecated("use v2")
                @GetMapping("/orders/legacy")
                fun legacyOrders(response: HttpServletResponse): String {
                    response.setHeader("Sunset", "Wed, 01 Jan 2027 00:00:00 GMT")
                    return orderService.fetchAll()
                }
            }
            """.trimIndent(),
        )
        assertTrue(KotlinDeprecationFinder.findAll(file).isEmpty())
    }

    fun `test a non-deprecated endpoint is never flagged`() {
        val file = myFixture.configureByText(
            "OrderController.kt",
            """
            class OrderController {
                @GetMapping("/orders")
                fun orders(): String {
                    return orderService.fetchAll()
                }
            }
            """.trimIndent(),
        )
        assertTrue(KotlinDeprecationFinder.findAll(file).isEmpty())
    }
}
