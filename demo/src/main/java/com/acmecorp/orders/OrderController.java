package com.acmecorp.orders;

public class OrderController {

    // Deprecated but the body never sets a deprecation header -- flagged.
    @Deprecated
    @GetMapping("/orders/legacy")
    String legacyOrders() {
        return orderService.fetchAll();
    }

    // Deprecated and correctly signals it on the wire -- not flagged.
    @Deprecated
    @GetMapping("/orders/legacy-v2")
    ResponseEntity<String> legacyOrdersV2(HttpServletResponse response) {
        response.setHeader("Deprecation", "true");
        response.setHeader("Sunset", "Wed, 01 Jan 2027 00:00:00 GMT");
        return ResponseEntity.ok(orderService.fetchAll());
    }
}
