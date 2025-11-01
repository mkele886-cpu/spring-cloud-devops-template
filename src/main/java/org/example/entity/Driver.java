package org.example.entity;

import org.example.interfaces.Vehicle;

public class Driver extends  PersonBase{


    @Override
    void speak() {
        System.out.println("我是司机 👨‍✈️");
    }


    public void drive(Vehicle vehicle) {
        //System.out.println("司机开始驾驶车辆...");
        speak();
        vehicle.run();
    }
}
