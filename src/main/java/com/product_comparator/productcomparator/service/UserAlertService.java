package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.entity.UserAlert;
import com.product_comparator.productcomparator.exception.product.ProductNotFoundException;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import com.product_comparator.productcomparator.repository.UserAlertRepository;
import com.product_comparator.productcomparator.util.EmailSender;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserAlertService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserAlertRepository userAlertRepository;
    @Autowired
    DiscountRepository  discountRepository;
    @Autowired
    EmailSender emailSender;
    public String addUserAlert(
            String userEmail,
            String productName,
            String productBrand,
            String productStore,
            BigDecimal priceSetpoint
    ) {
        Product product = productRepository.findTop1ByProductNameAndProductBrandAndStore(productName, productBrand, productStore);
        if (product == null) {
            throw new ProductNotFoundException("Product not found");
        }
        UserAlert alert = UserAlert.builder()
                .userEmail(userEmail)
                .product(product)
                .dateAdded(LocalDate.now())
                .sent(false)
                .priceSetpoint(priceSetpoint)
                .build();
        userAlertRepository.save(alert);
        return "success";
    }

    @Transactional
    @Scheduled(cron = "0 0 9 * * *")
    public void checkAndSendEmails() {
        List<UserAlert> userAlerts = userAlertRepository.findBySent(false);
        for (UserAlert userAlert : userAlerts) {
            Product product = userAlert.getProduct();
            Discount discount= discountRepository
                    .findTop1ByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStore(
                            product.getProductId(),
                            LocalDate.now(),
                            LocalDate.now(),
                            product.getStore()
                    );
            BigDecimal price = discount != null
                    ? BigDecimal.valueOf(product.getProductPrice())
                    .multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(discount.getPercentage()/100.00)))
                    : BigDecimal.valueOf(product.getProductPrice());
            if(price.compareTo(userAlert.getPriceSetpoint()) < 0 || price.compareTo(userAlert.getPriceSetpoint()) == 0) {
                System.out.println("Email sent to: " + userAlert.getUserEmail());
                //emailSender.sendSimpleMessage("georgeventel97@gmail.com", "testmail", "The product is here");
                userAlert.setSent(true);
                userAlert.setDateSent(LocalDate.now());
                userAlertRepository.save(userAlert);
            }
        }
    }
}
