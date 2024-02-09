package com.example.nymble.task.service;

import com.example.nymble.task.model.Activity;
import com.example.nymble.task.utils.Response;

public abstract class ActivityService {
    public abstract Response addActivity(Activity activity) ;

    public abstract Response signUpActivityByPassenger(String activityId, String passengerId);
}
