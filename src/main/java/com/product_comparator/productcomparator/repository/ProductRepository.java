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
    // find products by ID and order by the difference between the date field and target date
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

    // selects the products with the smallest price from the given date onwards
    @Query("""
    SELECT p
    FROM Product p
    WHERE p.date >= :date
    AND p.productPrice = (
        SELECT MIN(p2.productPrice)
        FROM Product p2
        WHERE p2.productId = p.productId AND p2.date >= :date
    )
    ORDER BY p.productPrice
""")
    List<Product> findLowestPricedProducts(@Param("date") LocalDate date);

    Product findTop1ByProductNameAndProductBrandAndStore(String productName, String productBrand, String store);
}
