package com.pahanaedu.enums;

public enum PaymentMethod {
    CASH,
    CREDIT_CARD,
    DEBIT_CARD,
    BANK_TRANSFER;

    public String getDisplayName() {
        switch (this) {
            case CASH:
                return "Cash";
            case CREDIT_CARD:
                return "Credit Card";
            case DEBIT_CARD:
                return "Debit Card";
            case BANK_TRANSFER:
                return "Bank Transfer";
            default:
                return this.name();
        }
    }

    public String getCssClass() {
        switch (this) {
            case CASH:
                return "payment-cash";
            case CREDIT_CARD:
                return "payment-credit-card";
            case DEBIT_CARD:
                return "payment-debit-card";
            case BANK_TRANSFER:
                return "payment-bank-transfer";
            default:
                return "payment-default";
        }
    }

    public static PaymentMethod fromString(String value) {
        if (value == null) {
            return null;
        }

        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.name().equalsIgnoreCase(value)) {
                return method;
            }
        }

        throw new IllegalArgumentException("Invalid payment method: " + value);
    }
}
