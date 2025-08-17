package com.pahanaedu.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentStatusTest {

    @Test
    void shouldHaveCorrectEnumValues() {
        PaymentStatus[] values = PaymentStatus.values();
        assertEquals(3, values.length);
        assertEquals(PaymentStatus.PENDING, values[0]);
        assertEquals(PaymentStatus.PAID, values[1]);
        assertEquals(PaymentStatus.CANCELLED, values[2]);
    }

    @Test
    void shouldGenerateDisplayNames() {
        assertEquals("Pending", PaymentStatus.PENDING.getDisplayName());
        assertEquals("Paid", PaymentStatus.PAID.getDisplayName());
        assertEquals("Cancelled", PaymentStatus.CANCELLED.getDisplayName());
    }

    @Test
    void shouldGenerateCssClasses() {
        assertEquals("status-pending", PaymentStatus.PENDING.getCssClass());
        assertEquals("status-paid", PaymentStatus.PAID.getCssClass());
        assertEquals("status-cancelled", PaymentStatus.CANCELLED.getCssClass());
    }

    @Test
    void shouldParseFromString() {
        assertEquals(PaymentStatus.PENDING, PaymentStatus.fromString("PENDING"));
        assertEquals(PaymentStatus.PAID, PaymentStatus.fromString("PAID"));
        assertNull(PaymentStatus.fromString(null));
    }

    @Test
    void shouldThrowExceptionForInvalidString() {
        assertThrows(IllegalArgumentException.class, () -> PaymentStatus.fromString("INVALID"));
    }
}
