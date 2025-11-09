// src/main/java/com/example/User.java
package com.example;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // <-- 핵심!
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {
    private String username;
    private int age;
}