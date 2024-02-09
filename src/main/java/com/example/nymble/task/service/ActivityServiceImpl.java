package com.example.nymble.task.service;

import com.example.nymble.task.model.Activity;
import com.example.nymble.task.model.Passenger;
import com.example.nymble.task.model.PassengerType;
import com.example.nymble.task.repo.ActivityRepo;
import com.example.nymble.task.repo.PassengerRepo;
import com.example.nymble.task.utils.Response;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class ActivityServiceImpl extends ActivityService {
    ActivityRepo activityRepo;
    PassengerRepo passengerRepo;

    public ActivityServiceImpl(ActivityRepo activityRepo,
                               PassengerRepo passengerRepo) {
        this.activityRepo = activityRepo;
        this.passengerRepo = passengerRepo;
    }

    @Override
    public Response addActivity(Activity activity) {
        Activity activityObj = new Activity();
        var response = new Response();
        activityObj.setName(activity.getName());
        activityObj.setCost(activity.getCost());
        activityObj.setDescription(activity.getDescription());
        activityObj.setCapacity(activity.getCapacity());
        activityObj.setDestinationId(activity.getDestinationId());

        if (activityRepo.addActivity(activityObj)) {
            response.setSuccess(true);
            response.setActivity(activityObj);
            return response;
        }

        response.setSuccess(false);
        response.setErrorMsg("Error while saving activity details");
        return response;

    }

    @Override
    public Response signUpActivityByPassenger(String activityId, String passengerId) {

        var response = new Response();
        Passenger passenger = passengerRepo.getPassengerDetailById(passengerId);
        Activity activity = activityRepo.getActivityById(activityId);

        int count = activityRepo.getTotalAssignActivity(activityId);
        //Checking if activity capacity exceed or not
        if (count >= activity.getCapacity()) {
            response.setSuccess(false);
            response.setErrorMsg("Activity capacity exceed");
            return response;
        }

        var passengerBalance = passenger.getBalance();
        var activityCost = activity.getCost();


        try {
            if (PassengerType.STANDARD == passenger.getType()) {
                if (passengerBalance >= activityCost) {
                    passengerBalance -= activityCost;
                    //Adding passenger into this activity and updating passenger balance
                    if (activityRepo.signUpActivityByPassenger(activityId, passengerId, passengerBalance)) {
                        response.setSuccess(true);
                        response.setActivity(activityRepo.getActivityById(activityId));
                        return response;
                    }
                }
            } else if (PassengerType.GOLD == passenger.getType()) {
                double discountedCost = activityCost * 0.9;
                if (passengerBalance >= discountedCost) {
                    passengerBalance -= discountedCost;
                    if (activityRepo.signUpActivityByPassenger(activityId, passengerId, passengerBalance)) {
                        response.setSuccess(true);
                        response.setActivity(activityRepo.getActivityById(activityId));
                        return response;
                    }
                    ;
                }
            } else if (PassengerType.PREMIUM == passenger.getType()) {
                if (activityRepo.signUpActivityByPassenger(activityId, passengerId)) {
                    response.setSuccess(true);
                    response.setActivity(activityRepo.getActivityById(activityId));
                    return response;
                }
            }
            response.setSuccess(false);
            response.setErrorMsg("Error while assigning activity to passenger");
            return response;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
