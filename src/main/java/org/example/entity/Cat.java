package org.example.entity;

public class Cat extends  Animal{
    public Cat(String name) {
        super(name);
    }

    @Override
    public void eat() {
        System.out.println(name + " 在吃鱼...");
    }

    public void meow() {
        System.out.println(name + " 在喵喵叫...");
    }
}
