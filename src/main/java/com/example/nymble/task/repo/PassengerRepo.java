package com.example.nymble.task.repo;

import com.example.nymble.task.model.Passenger;

import java.util.List;

public abstract class PassengerRepo {
    public abstract boolean addPassenger(Passenger passenger);

    public abstract int getPackageCapacityById(String travelPackageId);

    public abstract int getPassengerCountByPackageId(String travelPackageId);

    public abstract Passenger getPassengerDetailById(String passengerId);

    public abstract List<Passenger> getAllPassengerByPackageId(String packageId);
}
