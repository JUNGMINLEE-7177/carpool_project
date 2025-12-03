// src/main/java/com/example/Main.java
package com.example;

public class Main {
    public static void main(String[] args) {
        
        // @AllArgsConstructor 덕분에 생성자 사용 가능
        User user1 = new User("홍길동", 30);

        // @NoArgsConstructor + @Data (Setter)
        User user2 = new User();
        
        // 1. Setter 테스트 (setName, setAge가 없는데도 사용 가능)
        user2.setUsername("Lombok"); 
        user2.setAge(99);

        // 2. Getter 테스트 (getName, getAge가 없는데도 사용 가능)
        System.out.println("이름: " + user2.getUsername());
        System.out.println("나이: " + user2.getAge());

        // 3. toString() 테스트 (@Data가 자동 생성)
        System.out.println(user1.toString());
        System.out.println(user2.toString());
    }
}