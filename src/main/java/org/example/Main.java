package org.example;

import com.sun.org.apache.xpath.internal.functions.FuncTrue;
import org.example.entity.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //TIP 当文本光标位于高亮显示的文本处时按 <shortcut actionId="ShowIntentionActions"/>
        // 查看 IntelliJ IDEA 建议如何修正。
        //System.out.printf("Hello and welcome!");





        // 创建一个固定大小为 4 的线程池，适合 CPU 密集型任务
//        ExecutorService pool = Executors.newFixedThreadPool(4);
//        List<Future<String>> results = new ArrayList<>();
//        // 待分析的数据块及其大小（KB）
//        // Java 8 兼容方案 (可变 Map)
//        Map<Integer, Integer> dataChunks = new HashMap<>();
//        dataChunks.put(1, 5000);
//        dataChunks.put(2, 8000);
//        dataChunks.put(3, 2000);
//        dataChunks.put(4, 15000);
//        dataChunks.put(5, 6000);
//        System.out.println("--- 开始批量数据分析 (总共 " + dataChunks.size() + " 个数据块) ---");
//        // 提交所有数据分析任务
//        dataChunks.forEach((id, size) -> {
//            results.add(pool.submit(new DataAnalysisTask(id, size)));
//        });
//        // 阻塞并打印所有分析结果
//        for (Future<String> f : results) {
//            System.out.println(f.get());
//        }
//        pool.shutdown();
//        System.out.println("\n🎉 所有数据分析任务已完成！");






//        // 创建一个固定大小为 10 的线程池，提高并发请求量
//        ExecutorService pool = Executors.newFixedThreadPool(10);
//        List<Future<String>> results = new ArrayList<>();
//
//        // 待调用的接口或参数列表
//        String[] endpoints = {"/user/1", "/product/5", "/order/status", "/user/10", "/report/daily"};
//
//        System.out.println("--- 开始批量接口调用 (总共 " + endpoints.length + " 个接口) ---");
//
//        // 提交所有接口调用任务
//        for (String ep : endpoints) {
//            results.add(pool.submit(new ApiCallTask(ep)));
//        }
//
//        // 遍历结果，并处理可能的异常
//        for (Future<String> f : results) {
//            try {
//                System.out.println(f.get()); // f.get() 可能会抛出 ExecutionException
//            } catch (ExecutionException e) {
//                // 打印任务内部抛出的异常信息（例如上面的超时异常）
//                System.out.println(e.getCause().getMessage());
//            }
//        }
//
//        pool.shutdown();
//        System.out.println("\n🎉 所有接口调用任务已完成！");




//        // 创建一个固定大小为 5 的线程池，模拟带宽限制
//        ExecutorService pool = Executors.newFixedThreadPool(5);
//        List<Future<String>> results = new ArrayList<>();
//
//        // 待上传的文件列表
//        String[] files = {"合同1.pdf", "图片A.jpg", "报告Q3.docx", "日志2025.txt", "备份DB.zip", "附件6.zip"};
//
//        System.out.println("--- 开始批量文件上传 (总共 " + files.length + " 个文件) ---");
//
//        // 提交所有文件上传任务
//        for (String file : files) {
//            results.add(pool.submit(new FileUploadTask(file)));
//        }
//
//        // 阻塞并获取每个文件的上传结果
//        for (Future<String> f : results) {
//            System.out.println(f.get());
//        }
//
//        pool.shutdown();
//        System.out.println("\n🎉 所有文件上传任务已完成！");





//        ExecutorService pool = Executors.newFixedThreadPool(3);
//        List<Future<String>> results  = new ArrayList<>();
//
//        for(int i=1;i<=5;i++)
//        {
//           results.add(pool.submit(new TaskTest(i)));
//        }
//
//        for (Future<String> f: results)
//        {
//            System.out.println("任务结果: "+ f.get());
//        }
//        pool.shutdown();
//        System.out.println("所有任务都完成了！");




//  //并发集合类
//  Map<String, Integer> map = new ConcurrentHashMap<>();
//  for(int i=0; i<10; i++){
//      int value = i;
//      new Thread(()->{
//          map.put("【线程-"+ Thread.currentThread().getName()+"Key-】"+value,value);
//      }).start();
//  }
//    try { Thread.sleep(500); } catch (InterruptedException ignored) {}
//    System.out.println("并发Map内容：" + map);




//        Counter counter = new Counter();
//
//        Thread t1 = new Thread(() -> {
//            for (int i = 0; i < 10000; i++) {
//                counter.increment();
//            }
//        });
//
//        Thread t2 = new Thread(() -> {
//            for (int i = 0; i < 10000; i++) {
//                counter.increment();
//            }
//        });
//        t1.start();
//        t2.start();
//        t1.join();
//        t2.join();
//        System.out.println("最终计数值: " + counter.getCount());




        //固定3线程池,去处理5个任务，每个任务打印20次数字
        //线程池可重复使用线程，减少创建销毁开销
        //常用类型：newFixedThreadPool(n)、newCachedThreadPool()、newSingleThreadExecutor()

//        ExecutorService pool =Executors.newFixedThreadPool(3);
//        for(int i=1;i<=5;i++){
//             int taskId =i;
//            pool.submit(()->{
//                System.out.println("任务 "+ taskId+" 开始执行，线程: "+ Thread.currentThread().getName());
//                try {
//
//                    //每个任务具体做的事
//                    for (int j = 0; j < 20; j++) {
//                        System.out.println("任务 "+ taskId+"【内部任务】"+ Thread.currentThread().getName()+"正在执行:" + j);
//                    }
//                    Thread.sleep(1000); //模拟任务执行时间
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("任务 "+ taskId+" 执行完毕，线程: "+ Thread.currentThread().getName());
//            });
//        }
//        pool.shutdown();






//        //带返回值的任务：Callable + Future
//        Callable<Integer> task= ()->
//        {
//            System.out.println("计算中");
//            Thread.sleep(1000);
//            return 10 *10;
//        };
//
//        ExecutorService executor= Executors.newSingleThreadExecutor();
//        Future<Integer> future =executor.submit(task);
//
//        System.out.println("主线程在等待结果...");
//        Integer result = future.get(); //阻塞等待结果
//        System.out.println("计算结果是: "+ result);
//        executor.shutdown();





//        Thread t1=new Thread(new MyRunnable(),"线程1");
//        Thread t2=new Thread(new MyRunnable(),"线程2");
//
//        t1.start();
//        t2.start();






//        MyThread t1= new MyThread();
//        MyThread t2= new MyThread();
//
//        t1.setName("线程1");
//        t2.setName("线程2");
//
//        t1.start();
//        t2.start();
//
//        System.out.println("主线程结束");


//      String input = "D:\\upload\\scores.txt";
//      String output = "D:\\upload\\high_scores.txt";
//
//      try(BufferedReader reader = new BufferedReader(new FileReader(input));
//          BufferedReader writer = new BufferedReader(new FileReader(output))) {
//
//          List<String> highScores = reader.lines()
//                  .map(String::trim)
//                  .filter(line -> !line.isEmpty())
//                  .mapToInt(Integer::parseInt)
//                  .filter(score -> score >= 90)
//                  .mapToObj(String::valueOf)
//                  .collect(Collectors.toList());
//
//            Files.write(Paths.get(output), highScores);
//
//           System.out.println("高分学生已写入：" + output);
//
//          }
//      catch (IOException | NumberFormatException e) {
//          System.out.println("发生异常: " + e.getMessage());
//      }






//     try {
//         List<String> lines = Files.lines(Paths.get(filePath))
//                 .filter(line -> !line.trim().isEmpty()) // 过滤掉空行
//                 .map(String::toUpperCase) // 转换为大写
//                 .collect(Collectors.toList());
//            System.out.println("处理后的内容：");
//            lines.forEach(System.out::println);
//     }
//     catch (IOException e) {
//         e.printStackTrace();
//     }
















//        List<Integer> numbers = Arrays.asList(2,4,6,8,10,3,7);
//
//        //过滤出偶数，平方后求和
//        int sum = numbers.stream().filter(n -> n % 2 == 0)
//                .mapToInt(n -> n * n)
//                .sum();
//
//        System.out.println("偶数的平方和"+sum);
//
//
//        List<String> strList =numbers.stream()
//                .map(n -> "数字-" + n)
//                .sorted()
//                .collect(Collectors.toList());
//
//        System.out.println("处理后的字符串列表："+ strList);









//        try (FileInputStream in = new FileInputStream(srcfilePath);
//             FileOutputStream out = new FileOutputStream(destfilePath)) {
//
//            byte[] buffer = new byte[1024];
//            int len;
//            while ((len = in.read(buffer)) != -1) {
//                out.write(buffer, 0, len);
//            }
//            System.out.println("文件复制完成！");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }




//        //读取文件，自动关闭资源
//        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println("读取内容："+line);
//            }
//        } catch (IOException e) {
//            System.out.println("发生异常: " + e.getMessage());
//        }



//        //写入文件，自动关闭资源
//        try(FileWriter writer = new FileWriter(filePath)) {
//            writer.write("Hello, World! \n");
//            writer.write("第二行内容。\n");
//            System.out.println("文件写入成功");
//        } catch (IOException e) {
//            System.out.println("发生异常: " + e.getMessage());
//        }






//    try {
//        validateAge(200);
//    } catch (MyException e) {
//        System.out.println("无效的年龄: " + e.getMessage());
//    }



//      try
//      {
//          int a =10;
//          int b =0;
//          int c = a / b;
//          System.out.println("结果是: "+ c);
//      }
//      catch (ArithmeticException e)
//      {
//            System.out.println("发生异常: 除数不能为零 "+e.getMessage());
//      }
//      finally {
//            System.out.println("程序结束");
//      }





        //泛型的使用
//        List<String> names = new ArrayList<>();
//        names.add("Alice");
//        names.add("Bob");
//        names.add("Charlie");
//
//        //插入指定位置
//        names.add(1,"David");
//
//        //访问元素
//        System.out.println("第一个名字: " + names.get(0));
//
//        //删除元素
//        names.remove("Bob");
//
//        //遍历列表 普通for循环
//        for(int i=0;i< names.size();i++){
//            System.out.println("名字-循环1 "+i+" : "+ names.get(i));
//        }
//
//        //遍历列表 增强型for循环
//        for(String name:names){
//            System.out.println("名字-循环2: "+ name);
//        }
//
//        //forEach方法
//        names.forEach(name-> System.out.println("名字-循环3: "+ name));



//        Outer outer = new Outer();
//        outer.show();
//
//        //创建内部类对象
//        Outer.Inner inner = outer.new Inner();
//        inner.display();
//
//        //创建静态内部类对象
//        Outer.StaticInner staticInner = new Outer.StaticInner();
//        staticInner.display();


//        Driver driver =new Driver();
//        Vehicle car =new Car("宝马");
//        Vehicle bike =new Bike();
//
//        driver.drive(car);
//        driver.drive(bike);



//        Animal a1= new Dog("哈士奇");
//        Animal a2= new Cat("波斯猫");
//
//        //多态的使用场景
//        a1.eat();
//        a2.eat();
//
//        //向下转型
//        if(a1 instanceof Dog){
//            Dog dog=(Dog) a1;
//            dog.bark();
//        }



//        Dog dog = new Dog("旺财");
//        dog.eat();
//        dog.bark();
//
//        Cat cat = new Cat("咪咪");
//        cat.eat();
//        cat.meow();
//        cat.sleep();


        //StudentService service = new StudentService();

        //        Person p1=new Person("Alice",30);
        //        p1.sayHello();
        //        p1.setAge(35);
        //        System.out.printf("Alice 现在 %d 岁了%n",p1.getAge());




        /*
        while (true) {
            System.out.println("\\n==== 学生信息管理系统 ====");
            System.out.println("1. 添加学生");
            System.out.println("2. 查看学生");
            System.out.println("3. 删除学生");
            System.out.println("0. 退出");

            int choice = org.example.util.InputUtil.nextInt("请输入操作编号:");

            switch (choice) {
                case 1:
                    String id = org.example.util.InputUtil.nextLine("请输入学生ID:");
                    String name = org.example.util.InputUtil.nextLine("请输入学生姓名:");
                    int age = org.example.util.InputUtil.nextInt("请输入学生年龄:");
                    Student student = new Student(id, name, age);
                    service.addStudent(student);
                    break;
                case 2:
                    service.listStudents();
                    break;
                case 3:
                    String delId = org.example.util.InputUtil.nextLine("请输入要删除的学生ID:");
                    service.deleteStudent(delId);
                    break;
                case 0:
                    System.out.println("退出系统。");
                    return;
                default:
                    System.out.println("无效的操作编号，请重新输入。");
            }
        } */
    }
//    static void validateAge(int age) throws MyException  {
//        if (age < 0 || age > 150) {
//            throw new MyException("年龄必须在0到150之间");
//        } else {
//            System.out.println("年龄有效: " + age);
//        }
//    }
}