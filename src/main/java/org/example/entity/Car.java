package org.example.entity;

import org.example.interfaces.Vehicle;

public class Car implements Vehicle {
    private String brand;

    public Car(String brand) {
        this.brand = brand;
    }

    @Override
    public void run() {
        System.out.println(brand + " 汽车正在行驶...");
    }
}
