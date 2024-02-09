package com.example.nymble.task.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Destination {
    @Getter
    @Setter
    private String name;

    @Setter
    @Getter
    private List<Activity> activityList;
    @Setter
    @Getter
    private String destinationId;

    public Destination(String name) {
        this.name = name;
        this.activityList = new ArrayList<>();
    }

    public Destination() {
    }
}
