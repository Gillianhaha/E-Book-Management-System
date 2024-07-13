package com.group.eBookManagementSystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JavaSpringBootTestApplicationTests {

    @Test
    public void test() {
        int a = 4;
        int b = 6;
        int c = 10;
        assertEquals(a+b, c);
    }

}
