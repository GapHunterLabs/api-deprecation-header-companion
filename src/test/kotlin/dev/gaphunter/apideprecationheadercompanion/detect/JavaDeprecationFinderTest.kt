package dev.gaphunter.apideprecationheadercompanion.detect

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JavaDeprecationFinderTest : BasePlatformTestCase() {

    fun `test a deprecated endpoint with no header signal is flagged`() {
        val file = myFixture.configureByText(
            "OrderController.java",
            """
            class OrderController {
                @Deprecated
                @GetMapping("/orders/legacy")
                String legacyOrders() {
                    return orderService.fetchAll();
                }
            }
            """.trimIndent(),
        )
        assertEquals(1, JavaDeprecationFinder.findAll(file).size)
    }

    fun `test a deprecated endpoint setting the Deprecation header is not flagged`() {
        val file = myFixture.configureByText(
            "OrderController.java",
            """
            class OrderController {
                @Deprecated
                @GetMapping("/orders/legacy")
                ResponseEntity<String> legacyOrders(HttpServletResponse response) {
                    response.setHeader("Deprecation", "true");
                    return ResponseEntity.ok(orderService.fetchAll());
                }
            }
            """.trimIndent(),
        )
        assertTrue(JavaDeprecationFinder.findAll(file).isEmpty())
    }

    fun `test a non-deprecated endpoint is never flagged`() {
        val file = myFixture.configureByText(
            "OrderController.java",
            """
            class OrderController {
                @GetMapping("/orders")
                String orders() {
                    return orderService.fetchAll();
                }
            }
            """.trimIndent(),
        )
        assertTrue(JavaDeprecationFinder.findAll(file).isEmpty())
    }

    fun `test a deprecated method with no mapping annotation is not flagged`() {
        val file = myFixture.configureByText(
            "OrderService.java",
            """
            class OrderService {
                @Deprecated
                String fetchAllLegacy() {
                    return "legacy";
                }
            }
            """.trimIndent(),
        )
        assertTrue(JavaDeprecationFinder.findAll(file).isEmpty())
    }
}
