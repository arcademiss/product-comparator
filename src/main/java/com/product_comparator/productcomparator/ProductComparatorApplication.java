package com.product_comparator.productcomparator;

import com.product_comparator.productcomparator.util.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@EnableScheduling
@SpringBootApplication
public class ProductComparatorApplication {
    @Autowired
    private DataLoader dataLoader;
    public static void main(String[] args) {
        SpringApplication.run(ProductComparatorApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws IOException {
        dataLoader.init();
        System.out.println("Don1!");
    }

}
