package com.example.nymble.task.model;

public enum PassengerType {
    STANDARD(1),
    GOLD(2),
    PREMIUM(3);
    private int value;

    private PassengerType(int value) {
        this.value = value;
    }
}
