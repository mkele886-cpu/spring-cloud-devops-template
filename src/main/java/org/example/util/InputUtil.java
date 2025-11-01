package org.example.util;

import java.util.Scanner;

public class InputUtil {

    private  static final Scanner scanner=new Scanner(System.in);


    public static String nextLine(String msg){
        System.out.print(msg);
        return scanner.nextLine();
    }

    public static int nextInt(String msg){
        System.out.print(msg);
        while (true){
            try {
                return Integer.parseInt(scanner.nextLine());
            }catch (NumberFormatException e){
                System.out.print("输入有误，请输入一个整数:");
            }
        }
    }
}
