package com.coursework.smartcampus.repository;

import com.coursework.smartcampus.model.Room;
import com.coursework.smartcampus.model.Sensor;
import com.coursework.smartcampus.model.SensorReading;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {

    public static Map<String, Room> rooms = new HashMap<>();
    public static Map<String, Sensor> sensors = new HashMap<>();
    public static Map<String, List<SensorReading>> readings = new HashMap<>();

}