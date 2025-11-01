package org.example.entity;

import org.example.interfaces.Vehicle;

public class Bike implements Vehicle {
    @Override
    public void run() {
        System.out.println("自行车在骑行...");
    }
}
