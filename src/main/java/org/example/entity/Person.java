package org.example.entity;

public class Person {
    private  String name;
    private  int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }


    public void  sayHello(){
        System.out.printf("Hello, my name is %s, I am %d years old.%n",name,age);
    }

    public  int getAge()
    {
        return age;
    }

    public void  setAge(int age)
    {
        if(age>=0 && age <=150){
            this.age=age;
    }else
    {
     System.out.println("年龄不合法");}
    }

}
