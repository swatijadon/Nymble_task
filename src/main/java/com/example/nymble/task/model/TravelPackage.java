package com.example.nymble.task.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
public class TravelPackage {
    public String travelPackageId;
    public String name;
    public int passengerCapacity;
    public List<Destination> destinationList;
    public List<Passenger> passengersList;
    public Map<String, List<Activity>> destinationWithActivity;


    public TravelPackage(String name,
                         String travelPackageId,
                         int passengerCapacity,
                         List<Destination> destinationList,
                         List<Passenger> passengersList) {
        this.name = name;
        this.travelPackageId = travelPackageId;
        this.passengerCapacity = passengerCapacity;
        this.destinationList = destinationList;
        this.passengersList = passengersList;
    }

    public TravelPackage(){

    }



}
