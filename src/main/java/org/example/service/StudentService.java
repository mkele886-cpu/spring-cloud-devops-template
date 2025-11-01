package org.example.service;

import org.example.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentService {

    private final List<Student> students=new ArrayList<>();


    public void addStudent(Student s){
        students.add(s);
        System.out.println("添加成功");
    }


    public  void  listStudents()
    {
        if(students.isEmpty()){
            System.out.println("没有学生信息");
            return;
        }
        System.out.println("学生信息列表:");
        students.forEach(s-> System.out.printf("ID:%s, 姓名:%s, 年龄:%d%n",s.getId(),s.getName(),s.getAge()));
    }


    public void  deleteStudent(String id)
    {
        students.removeIf(s-> s.getId().equals(id));
        System.out.println("删除成功");

    }

}