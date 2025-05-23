package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.entity.UserAlert;
import com.product_comparator.productcomparator.exception.product.ProductNotFoundException;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import com.product_comparator.productcomparator.repository.UserAlertRepository;
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


    public String addUserAlert(
            String userEmail,
            String productName,
            String productBrand,
            String productStore,
            BigDecimal priceSetpoint
    ) {
        // find the product which matches the user alert
        Product product = productRepository.findTop1ByProductNameAndProductBrandAndStore(productName, productBrand, productStore);
        if (product == null) {
            throw new ProductNotFoundException("Product not found");
        }
        // create the user alert for the product
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
    // run this function every day at 9 am
    @Scheduled(cron = "0 0 9 * * *")
    public void checkAndSendEmails() {
        // find all unsent alerts
        List<UserAlert> userAlerts = userAlertRepository.findBySent(false);

        // loop through unsent alerts
        for (UserAlert userAlert : userAlerts) {
            // get the product associated with the alert
            Product product = userAlert.getProduct();

            // find the discount for the product
            Discount discount= discountRepository
                    .findTop1ByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStore(
                            product.getProductId(),
                            LocalDate.now(),
                            LocalDate.now(),
                            product.getStore()
                    );

            // compute the new price if it has a discount and keep the old price if not
            BigDecimal price = discount != null
                    ? BigDecimal.valueOf(product.getProductPrice())
                    .multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(discount.getPercentage()/100.00)))
                    : BigDecimal.valueOf(product.getProductPrice());

            // check if price point is met or lower
            if(price.compareTo(userAlert.getPriceSetpoint()) < 0 || price.compareTo(userAlert.getPriceSetpoint()) == 0) {

                // logic to send the email to the user with the alert
                System.out.println("Email sent to: " + userAlert.getUserEmail());

                // set the flag for sent true and set the date of the sending and update the database
                userAlert.setSent(true);
                userAlert.setDateSent(LocalDate.now());
                userAlertRepository.save(userAlert);
            }
        }
    }
}
