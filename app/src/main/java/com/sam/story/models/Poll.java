package com.sam.story.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Poll {

    private int round;
    private long timeCreated;
    private Long endTime = null;
    private boolean finished = false;

    public Poll() {
        // For firebase
    }

    public Poll(int round) {
        this.round = round;
        timeCreated = System.currentTimeMillis();
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public Long getEndTime() {
        return endTime;
    }

    public int getRound() {
        return round;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
