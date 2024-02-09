package com.example.nymble.task.repo;

import com.example.nymble.task.config.DataSourceConfig;
import com.example.nymble.task.model.Destination;
import com.example.nymble.task.model.TravelPackage;
import com.example.nymble.task.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Repository
public class TravelPackageRepoImpl extends TravelPackageRepo {


    private static final Logger logger =
            LoggerFactory.getLogger(TravelPackageRepo.class);
    private static final String INSERT = "INSERT INTO ";

    DataSourceConfig dataSource;

    public TravelPackageRepoImpl(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean addPackage(TravelPackage packages) throws SQLException {
        String query = INSERT +
                " Travel_package(package_id , name, passenger_capacity )"
                + " VALUES(?, ?, ?);";

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getDataSource().getConnection();
            connection.setAutoCommit(false);
            statement = dataSource.getPreparedStatement(query, connection);

            logger.debug("Query : {}", query);
            int i = 0;
            statement.setString(++i, packages.getTravelPackageId());
            statement.setString(++i, packages.getName());
            statement.setInt(++i, packages.getPassengerCapacity());


            if (statement.executeUpdate() == 1 &&
                    addDestination(packages.getDestinationList(),
                            packages.getTravelPackageId(),
                            connection)) {
                connection.commit();
                return true;
            }
            connection.rollback();
            return false;
        } catch (SQLException e) {
            logger.error("Error while creating package", e);
            connection.rollback();
            return false;
        } finally {
            connection.close();
            statement.close();
        }

    }

    @Override
    public TravelPackage getPackageById(String packageId) {
        String query ="SELECT p.name, p.passenger_capacity  FROM Travel_package p  WHERE package_id = ?";
        try (var con = dataSource.getDataSource().getConnection();
             var stmt = dataSource.getPreparedStatement(query, con)) {
            stmt.setString(1, packageId);
            var rs = stmt.executeQuery();
            return getResult(rs, packageId);
        } catch (SQLException e) {
            logger.debug("Error while getting passenger");
            return null;
        }
    }

    private TravelPackage getResult(ResultSet resultSet, String packageId) throws SQLException {
        TravelPackage travelPackage = new TravelPackage();
        while (resultSet.next()) {
            travelPackage.setTravelPackageId(packageId);
            travelPackage.setName(resultSet.getString("p.name"));
            travelPackage.setPassengerCapacity(resultSet.getInt("p.passenger_capacity"));
        }
        return travelPackage;
    }




    private boolean addDestination(List<Destination> destinationList,
                                   String travelPackageId,
                                   Connection connection) {
        String query = INSERT +
                " Destination(destination_id, destination_name, package_id)"
                + " VALUES(?, ?, ?);";
        try (PreparedStatement statement = dataSource.getPreparedStatement(query, connection)) {
            for (var i : destinationList) {
                statement.setString(1, Helper.getId());
                statement.setString(2, i.getName());
                statement.setString(3, travelPackageId);
                statement.addBatch();
            }
            int[] ints = statement.executeBatch();
            return ints.length == destinationList.size()
                    && Arrays.stream(ints).allMatch(i -> i == 1);
        } catch (SQLException e) {
            logger.error("Error while adding Destination", e);
            return false;
        }

    }
}
