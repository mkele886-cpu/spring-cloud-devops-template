package org.example.entity;

public class Animal {

    String name;

    public  Animal(String name){
        this.name=name;
    }

    public  void eat() {
        System.out.println(name + " 正在吃东西...");
    }

    public void sleep() {
        System.out.println(name + " 在睡觉...");
    }

}
