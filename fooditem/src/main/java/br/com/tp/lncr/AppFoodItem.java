package br.com.tp.lncr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.tp.lncr")
public class AppFoodItem {

    public static void main(String[] args) {
        SpringApplication.run(AppFoodItem.class, args);
    }
}
