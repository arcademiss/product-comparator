package com.product_comparator.productcomparator.repository;

import com.product_comparator.productcomparator.entity.Discount;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Integer> {
    boolean existsByProductIdAndStoreAndFromDate(String productId, String store, LocalDate fromDate);
    List<Discount> findByFromDateLessThanEqualAndToDateGreaterThanEqual(LocalDate fromDate, LocalDate toDate);
    List<Discount> findByProductIdAndFromDateLessThanEqualAndToDateGreaterThanEqual(
            String productId, LocalDate fromDate, LocalDate toDate
    );
}

