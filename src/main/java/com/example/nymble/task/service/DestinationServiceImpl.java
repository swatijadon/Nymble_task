package com.example.nymble.task.service;

import com.example.nymble.task.model.TravelPackage;
import com.example.nymble.task.repo.*;
import org.springframework.stereotype.Service;

@Service
public class DestinationServiceImpl extends DestinationService {
    private final TravelPackageRepo packageRepo;
    private final DestinationRepo destinationRepo;

    public DestinationServiceImpl(
            TravelPackageRepoImpl packageRepo,
            DestinationRepo repo) {
        this.packageRepo = packageRepo;
        this.destinationRepo = repo;

    }

    public TravelPackage getDestination(String packageId) {
        TravelPackage travelPackage = new TravelPackage();
        var packages = packageRepo.getPackageById(packageId);
        var  destinationList = destinationRepo.getDestinationByPackageId(packageId);
        travelPackage.setName(packages.getName());;
        travelPackage.setDestinationWithActivity(destinationList);

        return travelPackage;
    }
}
