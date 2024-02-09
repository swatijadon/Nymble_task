package com.example.nymble.task.controller;

import com.example.nymble.task.model.Activity;
import com.example.nymble.task.model.Destination;
import com.example.nymble.task.model.Passenger;
import com.example.nymble.task.model.TravelPackage;
import com.example.nymble.task.service.*;
import com.example.nymble.task.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Component
public class NymbleController {

    ActivityService activityService;

    DestinationService destinationService;

    TravelPackageService travelPackageService;

    PassengerService passengerService;

    @Autowired
    public NymbleController(ActivityServiceImpl activity,
                            DestinationServiceImpl destinationService,
                            TravelPackageServiceImpl travelPackage,
                            PassengerServiceImpl passengerService) {
        this.activityService = activity;
        this.destinationService = destinationService;
        this.travelPackageService = travelPackage;
        this.passengerService = passengerService;

    }


    @GetMapping(value = "/get/travelPackage/destination/{packageId}")
    private TravelPackage getTravelPackageDestination(@PathVariable("packageId") String packageId) {
        return destinationService.getDestination(packageId);

    }

    @RequestMapping(value = {"/add/package"}, method = RequestMethod.POST)
    private Response addTravelPackage(@RequestBody TravelPackage travelPackage) {
        //Adding travel Package with Destination
        return travelPackageService.createPackage(travelPackage);

    }

    @RequestMapping(value = {"/add/activity"}, method = RequestMethod.POST)
    private Response addActivity(@RequestBody Activity activity) {
        //Adding Activity with belongs to destination
        return activityService.addActivity(activity);

    }

    @RequestMapping(value = {"/add/passenger"}, method = RequestMethod.POST)
    private Response addPassengerToPackage(@RequestBody Passenger passenger) {
        //Adding Passenger to travel package using package id
        return passengerService.addPassenger(passenger);

    }


    //Sign up passenger for activity using activity id and passenger id
    @RequestMapping(value = {"/sign/up/activity/{activityId}/{passengerId}"}, method = RequestMethod.POST)
    public Response signUpForActivity(@PathVariable("activityId") String activityId, @PathVariable("passengerId") String passengerId) {
        return activityService.signUpActivityByPassenger(activityId, passengerId);
    }

    //Print the passenger list of the travel package as Per the requirement
    @RequestMapping(value = {"/get/passenger/list/{packageId}"}, method = RequestMethod.GET)
    public TravelPackage GettingPassengerList(@PathVariable("packageId") String packageId) {
        return passengerService.getPassengerAllDetails(packageId);
    }

    //Print the individual passenger details
    @RequestMapping(value = {"/get/passenger/{passengerId}"}, method = RequestMethod.GET)
    public Passenger GettingPassengerById(@PathVariable("passengerId") String passengerId) {
        return passengerService.getPassengerDetailsById(passengerId);
    }

}
