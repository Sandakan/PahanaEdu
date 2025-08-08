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

    public String getIconClass() {
        switch (this) {
            case CASH:
                return "icon-cash";
            case CREDIT_CARD:
                return "icon-credit-card";
            case DEBIT_CARD:
                return "icon-debit-card";
            case BANK_TRANSFER:
                return "icon-bank-transfer";
            default:
                return "icon-payment";
        }
    }
}
