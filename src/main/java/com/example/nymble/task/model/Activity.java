package com.example.nymble.task.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.List;

@Entity
@Setter
@Getter
public class Activity {
    String destinationId;
    public String name;
    public String description;
    double cost;
    int capacity;
    List<Passenger> passengerList;
    String errorMsg;
    String activityId;
    String passengerId;
    String DestinationName;
}
