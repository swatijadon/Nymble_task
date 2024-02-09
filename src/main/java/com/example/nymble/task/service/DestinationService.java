package com.example.nymble.task.service;

import com.example.nymble.task.model.Destination;
import com.example.nymble.task.model.TravelPackage;

import java.util.List;

public abstract class DestinationService {
    public abstract TravelPackage getDestination(String name);
}
