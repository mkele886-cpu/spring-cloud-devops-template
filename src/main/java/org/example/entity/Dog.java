package org.example.entity;

public class Dog extends  Animal {

    public Dog(String name) {
        super(name);
    }

    @Override
    public void eat() {
        System.out.println(name + " 正在吃狗粮...");
    }

    public void bark() {
        System.out.println(name + " 在汪汪叫...");
    }
}
