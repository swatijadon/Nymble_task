package com.example.nymble.task.repo;

import com.example.nymble.task.config.DataSourceConfig;
import com.example.nymble.task.model.Activity;
import com.example.nymble.task.model.Passenger;
import com.example.nymble.task.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ActivityRepoImpl extends ActivityRepo {
    private static final String INSERT = "INSERT INTO ";
    private static final Logger logger =
            LoggerFactory.getLogger(ActivityRepoImpl.class);
    DataSourceConfig dataSource;

    public ActivityRepoImpl(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean addActivity(Activity activityObj) {
        String query = INSERT +
                " Activity(activity_id, activity_name, description, cost, capacity, destination_id)"
                + " VALUES(?, ?, ?, ?, ?, ?);";

        try (var con = dataSource.getDataSource().getConnection();
             var stmt = dataSource.getPreparedStatement(query, con)) {
            stmt.setString(1, Helper.getId());
            stmt.setString(2, activityObj.getName());
            stmt.setString(3, activityObj.getDescription());
            stmt.setDouble(4, activityObj.getCost());
            stmt.setInt(5, activityObj.getCapacity());
            stmt.setString(6, activityObj.getDestinationId());

            if (stmt.executeUpdate() == 1) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            logger.debug("Error while saving activity");
            return false;
        }
    }

    @Override
    public Activity getActivityById(String activityId) {
        String query = "SELECT a.activity_id , a.activity_name, a.description, a.cost ,a.destination_id  " +
                "from Activity a where a.activity_id =?;";
        try (var con = dataSource.getDataSource().getConnection();
             var stmt = dataSource.getPreparedStatement(query, con)) {
            stmt.setString(1, activityId);
            var rs = stmt.executeQuery();
            return getResult(rs);
        } catch (SQLException e) {
            logger.debug("Error while getting passenger");
            return null;
        }
    }

    @Override
    public boolean signUpActivityByPassenger(String activityId, String passengerId, double passengerBalance) throws SQLException {
        try (var con = dataSource.getDataSource().getConnection()) {
            con.setAutoCommit(false);
            if (assignActivityToPassenger(passengerId, activityId, con) &&
                    updatePassengerBalance(passengerId, passengerBalance, con)) {
                con.commit();
                return true;
            } else {
                con.rollback();
                return false;
            }
        } catch (SQLException e) {
            logger.debug("Error while updating activity ");
            return false;
        }
    }

    private boolean assignActivityToPassenger(String passengerId, String activityId, Connection con) {
        String query = INSERT +
                " Passenger_associate_activity(passenger_activity_id, passenger_id, activity_id) VALUES(?, ?, ?)";
        try (PreparedStatement statement = dataSource.getPreparedStatement(query, con)) {
            statement.setString(1, Helper.getId());
            statement.setString(2, passengerId);
            statement.setString(3, activityId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            logger.error("Error while updating balance", e);
            return false;
        }
    }

    @Override
    public boolean signUpActivityByPassenger(String activityId, String passengerId) {
        String query = "UPDATE Activity SET passenger_id= " +
                " where activity_id = ?;";
        try (Connection con = dataSource.getDataSource().getConnection();
             PreparedStatement statement = dataSource.getPreparedStatement(query, con)) {
            statement.setString(1, passengerId);
            statement.setString(2, activityId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            logger.error("Error while assigning activity to passenger", e);
            return false;
        }
    }

    @Override
    public int getTotalAssignActivity(String activityId) {
        String query = "SELECT count(a.activity_id) as activity from Activity a " +
                "left join Passenger_associate_activity paa on a.activity_id  = paa.activity_id " +
                "where a.activity_id =? ";
        try (Connection con = dataSource.getDataSource().getConnection();
             PreparedStatement statement = dataSource.getPreparedStatement(query, con)) {
            statement.setString(1, activityId);
            var rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("activity");
            }
            return 0;
        } catch (SQLException e) {
            logger.error("Error while checking activity capacity", e);
            return 0;
        }
    }

    @Override
    public List<Activity> getActivityByPassenger(String passengerId) {
        String query = "SELECT a.activity_name , a.cost, d.destination_name" +
                " From Passenger_associate_activity paa " +
                "LEFT JOIN Activity a on a.activity_id =paa.activity_id " +
                "LEFT JOIN Destination d on d.destination_id = a.destination_id "+
                "WHERE passenger_id = ?";
        try (var con = dataSource.getDataSource().getConnection();
             var stmt = dataSource.getPreparedStatement(query, con)) {
            stmt.setString(1, passengerId);
            var rs = stmt.executeQuery();
            return getResults(rs);
        } catch (SQLException e) {
            logger.debug("Error while getting activity List");
            return null;
        }
    }

    private List<Activity> getResults(ResultSet rs) throws SQLException {
        List<Activity> activityList = new ArrayList<>();
        while (rs.next()) {
            Activity activity = new Activity();
            activity.setName(rs.getString("a.activity_name"));
            activity.setCost(rs.getDouble("a.cost"));
            activity.setDestinationName(rs.getString("d.destination_name"));
            activityList.add(activity);

        }
        return activityList;

    }

    private boolean updatePassengerBalance(String passengerId, double passengerBalance, Connection con) {

        String query = "UPDATE Passenger SET balance = ? where passenger_id = ?;";
        try (PreparedStatement statement = dataSource.getPreparedStatement(query, con)) {
            statement.setDouble(1, passengerBalance);
            statement.setString(2, passengerId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            logger.error("Error while updating balance", e);
            return false;
        }
    }

    private Activity getResult(ResultSet resultSet) throws SQLException {
        var activity = new Activity();
        while (resultSet.next()) {
            activity.setActivityId(resultSet.getString("a.activity_id"));
            activity.setName(resultSet.getString("a.activity_name"));
            activity.setDescription(resultSet.getString("a.description"));
            activity.setCost(resultSet.getInt("a.cost"));
            activity.setDestinationId(resultSet.getString("a.destination_id"));
        }
        return activity;
    }
}
