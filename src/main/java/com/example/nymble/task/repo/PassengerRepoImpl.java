package com.example.nymble.task.repo;

import com.example.nymble.task.config.DataSourceConfig;
import com.example.nymble.task.model.Passenger;
import com.example.nymble.task.model.PassengerType;
import com.example.nymble.task.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PassengerRepoImpl extends PassengerRepo {

    private static final Logger logger =
            LoggerFactory.getLogger(PassengerRepoImpl.class);
    private static final String INSERT = "INSERT INTO ";

    DataSourceConfig dataSource;

    public PassengerRepoImpl(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean addPassenger(Passenger passenger) {
        String query = INSERT +
                " Passenger(passenger_id, name, passenger_number, Travel_package_id, balance , passanger_type)"
                + " VALUES(?, ?, ?, ?, ?, ?);";

        try (var con = dataSource.getDataSource().getConnection();
             var stmt = dataSource.getPreparedStatement(query, con)) {
            stmt.setString(1, Helper.getId());
            stmt.setString(2, passenger.getName());
            stmt.setInt(3, passenger.getPassengerNum());
            stmt.setString(4, passenger.getTravelPackageId());
            stmt.setDouble(5, passenger.getBalance());
            stmt.setString(6, passenger.getType().name());

            if (stmt.executeUpdate() == 1) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            logger.debug("Error while saving Passenger");
            return false;
        }
    }

    @Override
    public int getPackageCapacityById(String travelPackageId) {
        String query = "SELECT tp.passenger_capacity  FROM Travel_package tp " +
                "WHERE tp.package_id = ?";
        try (var con = dataSource.getDataSource().getConnection();
             var stmt = dataSource.getPreparedStatement(query, con)) {
            stmt.setString(1, travelPackageId);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("passenger_capacity");

            }
            return 0;
        } catch (SQLException e) {
            logger.debug("Error while saving activity");
            return 0;
        }

    }

    @Override
    public int getPassengerCountByPackageId(String travelPackageId) {
        String query = "SELECT COUNT(*) AS passenger FROM Passenger p where p.Travel_package_id = ?";
        try (var con = dataSource.getDataSource().getConnection();
             var stmt = dataSource.getPreparedStatement(query, con)) {
            stmt.setString(1, travelPackageId);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("passenger");
            }
            return 0;
        } catch (SQLException e) {
            logger.debug("Error while saving activity");
            return 0;
        }
    }

    @Override
    public Passenger getPassengerDetailById(String passengerId) {
        String query =
                "SELECT p.passenger_id , p.name, p.passenger_number, p.Travel_package_id, p.passanger_type," +
                        "  p.balance FROM Passenger p WHERE p.passenger_id = ?;";
        try (var con = dataSource.getDataSource().getConnection();
             var stmt = dataSource.getPreparedStatement(query, con)) {
            stmt.setString(1, passengerId);
            var rs = stmt.executeQuery();
            return getResult(rs);
        } catch (SQLException e) {
            logger.debug("Error while getting passenger");
            return null;
        }
    }

    @Override
    public List<Passenger> getAllPassengerByPackageId(String packageId) {
        String query = "SELECT p.passenger_id, p.name , p.passenger_number , p.passanger_type, p .balance  " +
                "FROM Passenger p where p.Travel_package_id =?";

        try (var con = dataSource.getDataSource().getConnection();
             var stmt = dataSource.getPreparedStatement(query, con)) {
            stmt.setString(1, packageId);
            var rs = stmt.executeQuery();
            return getResults(rs);
        } catch (SQLException e) {
            logger.debug("Error while getting passenger");
            return null;
        }
    }

    private List<Passenger> getResults(ResultSet rs) throws SQLException {
        List<Passenger> passengers = new ArrayList<>();
        while (rs.next()) {
            Passenger p = new Passenger();
            p.setName(rs.getString("p.name"));
            p.setPassengerNum(rs.getInt("p.passenger_number"));
            passengers.add(p);

        }
       return passengers;
    }

    private Passenger getResult(ResultSet resultSet) throws SQLException {
        Passenger passenger = new Passenger();
        while (resultSet.next()) {
            passenger.setPassengerId(resultSet.getString("p.passenger_id"));
            passenger.setName(resultSet.getString("p.name"));
            passenger.setPassengerNum(resultSet.getInt("p.passenger_number"));
            passenger.setTravelPackageId(resultSet.getString("p.Travel_package_id"));
            passenger.setType(PassengerType.valueOf(resultSet.getString("p.passanger_type")));
            passenger.setBalance(resultSet.getDouble("p.balance"));
        }
        return passenger;
    }
}
