package com.example.nymble.task.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
public class Passenger {
    private String name;
    private int passengerNum;
    private double balance;
    private List<Activity> activityList;

    private String travelPackageId;

    private PassengerType type;
    private String passengerId;

}
