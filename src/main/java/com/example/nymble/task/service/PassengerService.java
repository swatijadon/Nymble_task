package com.example.nymble.task.service;

import com.example.nymble.task.model.Passenger;
import com.example.nymble.task.model.TravelPackage;
import com.example.nymble.task.utils.Response;

public abstract class PassengerService {
    public abstract Response addPassenger(Passenger passenger);

    public abstract TravelPackage getPassengerAllDetails(String packageId);

    public abstract Passenger getPassengerDetailsById(String passengerId);
}
