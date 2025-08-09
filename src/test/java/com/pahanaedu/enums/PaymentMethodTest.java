package com.pahanaedu.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {

    @Test
    void shouldHaveCorrectEnumValues() {
        PaymentMethod[] values = PaymentMethod.values();
        assertEquals(4, values.length);
    }

    @Test
    void shouldGenerateDisplayNames() {
        assertEquals("Cash", PaymentMethod.CASH.getDisplayName());
        assertEquals("Credit Card", PaymentMethod.CREDIT_CARD.getDisplayName());
        assertEquals("Debit Card", PaymentMethod.DEBIT_CARD.getDisplayName());
        assertEquals("Bank Transfer", PaymentMethod.BANK_TRANSFER.getDisplayName());
    }

    @Test
    void shouldGenerateCssClasses() {
        assertEquals("payment-cash", PaymentMethod.CASH.getCssClass());
        assertEquals("payment-credit-card", PaymentMethod.CREDIT_CARD.getCssClass());
        assertEquals("payment-debit-card", PaymentMethod.DEBIT_CARD.getCssClass());
        assertEquals("payment-bank-transfer", PaymentMethod.BANK_TRANSFER.getCssClass());
    }

    @Test
    void shouldParseFromString() {
        assertEquals(PaymentMethod.CASH, PaymentMethod.fromString("CASH"));
        assertEquals(PaymentMethod.CREDIT_CARD, PaymentMethod.fromString("CREDIT_CARD"));
        assertNull(PaymentMethod.fromString(null));
    }

    @Test
    void shouldThrowExceptionForInvalidString() {
        assertThrows(IllegalArgumentException.class, () -> PaymentMethod.fromString("INVALID"));
    }
}
