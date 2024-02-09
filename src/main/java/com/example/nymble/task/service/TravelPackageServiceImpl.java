package com.example.nymble.task.service;

import com.example.nymble.task.model.Destination;
import com.example.nymble.task.model.TravelPackage;
import com.example.nymble.task.repo.TravelPackageRepo;
import com.example.nymble.task.repo.TravelPackageRepoImpl;
import com.example.nymble.task.utils.Helper;
import com.example.nymble.task.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class TravelPackageServiceImpl extends TravelPackageService{

    private static final Logger logger = LoggerFactory.getLogger(TravelPackageServiceImpl.class);
    private final TravelPackageRepo repo;

    public TravelPackageServiceImpl(TravelPackageRepoImpl repo) {
        this.repo = repo;
    }


    public Response createPackage(TravelPackage travelPackage) {
        try {
            var createPackage = new TravelPackage();
            var response = new Response();
            createPackage.setTravelPackageId(Helper.getId());
            createPackage.setName(travelPackage.getName());
            createPackage.setPassengerCapacity(travelPackage.getPassengerCapacity());
            createPackage.setDestinationList(travelPackage.getDestinationList());

            if (repo.addPackage(createPackage)) {
                response.setSuccess(true);
                response.setTravelPackage(createPackage);
                return response;
            }
            response.setSuccess(false);
            response.setErrorMsg("Error while saving package details");
            return response;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
