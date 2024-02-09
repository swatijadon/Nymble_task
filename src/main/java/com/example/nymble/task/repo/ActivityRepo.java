package com.example.nymble.task.repo;

import com.example.nymble.task.model.Activity;

import java.sql.SQLException;
import java.util.List;

public abstract class ActivityRepo {
    public abstract boolean addActivity(Activity activityObj);

    public abstract Activity getActivityById(String activityId);

    public abstract boolean signUpActivityByPassenger(String activityId, String passengerId, double passengerBalance) throws SQLException;

    public abstract boolean signUpActivityByPassenger(String activityId, String passengerId);

    public abstract int getTotalAssignActivity(String activityId);

    public abstract List<Activity> getActivityByPassenger(String passengerId);
}
