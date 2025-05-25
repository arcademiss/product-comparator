package com.product_comparator.productcomparator.service;

import com.product_comparator.productcomparator.entity.Discount;
import com.product_comparator.productcomparator.entity.Product;
import com.product_comparator.productcomparator.entity.UserAlert;
import com.product_comparator.productcomparator.exception.product.ProductNotFoundException;
import com.product_comparator.productcomparator.repository.DiscountRepository;
import com.product_comparator.productcomparator.repository.ProductRepository;
import com.product_comparator.productcomparator.repository.UserAlertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAlertService {

    private final ProductRepository productRepository;
    private final UserAlertRepository userAlertRepository;
    private final DiscountRepository discountRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserAlertService.class);

    public String addUserAlert(String userEmail, String productName, String productBrand, String productStore, BigDecimal priceSetpoint) {
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
        LocalDate today = LocalDate.now();

        for (UserAlert userAlert : userAlerts) {
            Product product = userAlert.getProduct();

            Discount discount = discountRepository
                    .findTop1ByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStore(
                            product.getProductId(), today, today, product.getStore()
                    );

            BigDecimal effectivePrice = getEffectivePrice(product, discount);

            if (effectivePrice.compareTo(userAlert.getPriceSetpoint()) <= 0) {
                logger.info("Email sent to: {}", userAlert.getUserEmail());

                userAlert.setSent(true);
                userAlert.setDateSent(today);
                userAlertRepository.save(userAlert);
            }
        }
    }

    private BigDecimal getEffectivePrice(Product product, Discount discount) {
        BigDecimal basePrice = BigDecimal.valueOf(product.getProductPrice());
        if (discount == null) return basePrice;

        BigDecimal discountFactor = BigDecimal.valueOf(discount.getPercentage())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return basePrice.multiply(BigDecimal.ONE.subtract(discountFactor));
    }
}
