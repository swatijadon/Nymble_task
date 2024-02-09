package com.example.nymble.task.utils;

import com.example.nymble.task.model.Activity;
import com.example.nymble.task.model.Passenger;
import com.example.nymble.task.model.TravelPackage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Response {

    private String errorMsg;

    private TravelPackage travelPackage;
    private Activity activity;
    private Passenger passenger;

    private boolean success;

    public Response() {

    }

}
