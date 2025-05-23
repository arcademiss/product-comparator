package com.product_comparator.productcomparator;

import com.product_comparator.productcomparator.service.UserAlertService;
import com.product_comparator.productcomparator.util.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.io.IOException;

@EnableScheduling
@SpringBootApplication
public class ProductComparatorApplication {
    @Autowired
    private DataLoader dataLoader;
    @Autowired
    private UserAlertService userAlertService;
    public static void main(String[] args) {
        SpringApplication.run(ProductComparatorApplication.class, args);
    }

    // run after app is built
    @EventListener(ApplicationReadyEvent.class)
    public void init() throws IOException {
        userAlertService.checkAndSendEmails();
        dataLoader.init();

    }

}
