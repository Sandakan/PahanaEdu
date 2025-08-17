package com.pahanaedu.enums;

public enum PaymentStatus {
    PENDING,
    PAID,
    CANCELLED;

    public String getDisplayName() {
        switch (this) {
            case PENDING:
                return "Pending";
            case PAID:
                return "Paid";
            case CANCELLED:
                return "Cancelled";
            default:
                return this.name();
        }
    }

    public String getCssClass() {
        switch (this) {
            case PENDING:
                return "status-pending";
            case PAID:
                return "status-paid";
            case CANCELLED:
                return "status-cancelled";
            default:
                return "status-default";
        }
    }

    public static PaymentStatus fromString(String value) {
        if (value == null) {
            return null;
        }

        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Invalid payment status: " + value);
    }
}
