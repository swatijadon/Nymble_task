package com.example.nymble.task.service;

import com.example.nymble.task.model.TravelPackage;
import com.example.nymble.task.utils.Response;

public abstract  class TravelPackageService {
    public abstract Response createPackage(TravelPackage travelPackage);
}
