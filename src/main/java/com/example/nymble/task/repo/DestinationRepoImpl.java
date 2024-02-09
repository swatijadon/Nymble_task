package com.example.nymble.task.repo;

import com.example.nymble.task.config.DataSourceConfig;
import com.example.nymble.task.model.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DestinationRepoImpl extends DestinationRepo {

    private static final String INSERT = "INSERT INTO ";
    private static final Logger logger =
            LoggerFactory.getLogger(DestinationRepoImpl.class);
    DataSourceConfig dataSource;

    public DestinationRepoImpl(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Map<String, List<Activity>> getDestinationByPackageId(String packageId) {
        String query = "SELECT d.destination_name, d.destination_id, a.activity_id, a.activity_name , a.cost," +
                " a.capacity, a.description  from Destination d " +
                "left join Activity a on a.destination_id =d.destination_id " +
                "where d.package_id =?";
        try (var con = dataSource.getDataSource().getConnection();
             var stmt = dataSource.getPreparedStatement(query, con)) {
            stmt.setString(1, packageId);
            var rs = stmt.executeQuery();
            return getResults(rs);
        } catch (SQLException e) {
            logger.debug("Error while getting destination");
            return null;
        }

    }

    private Map<String, List<Activity>> getResults(ResultSet rs) throws SQLException {
        Map<String, List<Activity>> destinationActivitiesMap = new HashMap<>();
        while (rs.next()) {
            String destinationName = rs.getString("d.destination_name");
            Activity activity = new Activity();
            activity.setActivityId(rs.getString("a.activity_id"));
            activity.setName(rs.getString("a.activity_name"));
            activity.setCost(rs.getDouble("a.cost"));
            activity.setCapacity(rs.getInt("a.capacity"));
            activity.setDestinationName(rs.getString("a.description"));

            // Retrieve or create activity list for the destination
            List<Activity> activityList = destinationActivitiesMap.getOrDefault(destinationName, new ArrayList<>());
            activityList.add(activity);
            destinationActivitiesMap.put(destinationName, activityList);

        }
        return destinationActivitiesMap;

    }

}
