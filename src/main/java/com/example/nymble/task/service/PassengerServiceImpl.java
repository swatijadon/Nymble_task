package com.example.nymble.task.service;

import com.example.nymble.task.model.Activity;
import com.example.nymble.task.model.Passenger;
import com.example.nymble.task.model.TravelPackage;
import com.example.nymble.task.repo.*;
import com.example.nymble.task.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerServiceImpl extends PassengerService {
    private static final Logger logger = LoggerFactory.getLogger(TravelPackageServiceImpl.class);
    private final PassengerRepo repo;

    private final TravelPackageRepo packageRepo;

    private final ActivityRepo activityRepo;

    public PassengerServiceImpl(PassengerRepoImpl repo,
                                TravelPackageRepoImpl packageRepo,
                                ActivityRepoImpl activityRepo) {
        this.repo = repo;
        this.packageRepo = packageRepo;
        this.activityRepo = activityRepo;
    }

    @Override
    public Response addPassenger(Passenger passenger) {
        var response = new Response();
        int packageCapacity = repo.getPackageCapacityById(passenger.getTravelPackageId());
        int count = repo.getPassengerCountByPackageId(passenger.getTravelPackageId());

        if (count >= packageCapacity) {
            response.setSuccess(false);
            response.setErrorMsg("Passenger capacity exceed");
        }

        var passengerObj = new Passenger();
        passengerObj.setName(passenger.getName());
        passengerObj.setPassengerNum(passenger.getPassengerNum());
        passengerObj.setBalance(passenger.getBalance());
        passengerObj.setTravelPackageId(passenger.getTravelPackageId());
        passengerObj.setType(passenger.getType());
        if (repo.addPassenger(passengerObj)) {
            response.setSuccess(true);
            response.setPassenger(passenger);
            return response;
        }
        response.setSuccess(false);
        response.setErrorMsg("Error while saving package details");
        return response;
    }

    @Override
    public TravelPackage getPassengerAllDetails(String packageId) {
        var travelPackage = new TravelPackage();
        TravelPackage packageObj = packageRepo.getPackageById(packageId);

        List<Passenger> passengerList = repo.getAllPassengerByPackageId(packageId);
        travelPackage.setName(packageObj.getName());
        travelPackage.setPassengerCapacity(packageObj.getPassengerCapacity());
        travelPackage.setPassengersList(passengerList);

        return travelPackage;
    }

    @Override
    public Passenger getPassengerDetailsById(String passengerId) {

        var passenger = new Passenger();
        var passengerObj = repo.getPassengerDetailById(passengerId);

        passenger.setName(passengerObj.getName());
        passenger.setPassengerNum(passengerObj.getPassengerNum());
        passenger.setBalance(passengerObj.getBalance());
        var activityList = activityRepo.getActivityByPassenger(passengerId);

        passenger.setActivityList(activityList);

        return passenger;
    }
}
