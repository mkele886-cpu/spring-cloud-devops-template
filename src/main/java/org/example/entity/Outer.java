package org.example.entity;

public class Outer {
    private String msg = "Hello from Outer class";

    public void show() {
        System.out.println("这是外部类方法");
    }

  public  class Inner {
        public void display() {
            System.out.println("访问外部类成员："+msg);
        }
    }


   public static  class  StaticInner {
        public void display() {
            System.out.println("这是静态内部类方法");
        }
    }


}
