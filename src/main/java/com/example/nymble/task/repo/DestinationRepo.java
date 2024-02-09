package com.example.nymble.task.repo;

import com.example.nymble.task.model.Activity;

import java.util.List;
import java.util.Map;

public abstract class DestinationRepo {
    public abstract Map<String, List<Activity>> getDestinationByPackageId(String packageId);
}
