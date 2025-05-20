package com.product_comparator.productcomparator.repository;

import com.product_comparator.productcomparator.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsByProductIdAndStoreAndDate(String productId, String store, LocalDate date);

    List<Product> findByProductNameContainingIgnoreCase(String name);
    @Query(
            value = "SELECT * FROM product WHERE product_id = :productId " +
                    "ORDER BY ABS((date - CAST(:targetDate AS DATE)))" +
                    "LIMIT 1",
            nativeQuery = true
    )
    Optional<Product> findClosestByProductIdAndDateNative(
            @Param("productId") String productId,
            @Param("targetDate") LocalDate targetDate
    );
}
