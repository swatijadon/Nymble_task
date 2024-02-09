package com.example.nymble.task.repo;

import com.example.nymble.task.model.TravelPackage;

import java.sql.SQLException;

public abstract class TravelPackageRepo {


    public abstract boolean addPackage(TravelPackage packages) throws SQLException;

    public abstract TravelPackage getPackageById(String packageId);
}
