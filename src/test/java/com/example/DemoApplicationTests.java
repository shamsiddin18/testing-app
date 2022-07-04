package com.example;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class DemoApplicationTests {

    Calculator underTest = new Calculator();

    @Test
    void itShouldAddNumbers(){

        int numberOne=20;
        int numberTwo=30;

        int result = underTest.add(numberOne,numberTwo);

        assertThat(result).isEqualTo(50);
       }



    class Calculator{
        int add(int a, int b){
            return a+b;
        }
    }



}
