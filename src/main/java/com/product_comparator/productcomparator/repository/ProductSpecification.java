package com.product_comparator.productcomparator.repository;

import com.product_comparator.productcomparator.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> hasName(String name) {
        return (root, query, cb) -> name == null || name.isBlank()
                ? null
                : cb.equal(cb.lower(root.get("productName")), name.toLowerCase());
    }

    public static Specification<Product> hasStore(String store) {
        return (root, query, cb) -> store == null || store.isBlank()
                ? null
                : cb.equal(cb.lower(root.get("store")), store.toLowerCase());
    }

    public static Specification<Product> hasBrand(String brand) {
        return (root, query, cb) -> brand == null || brand.isBlank()
                ? null
                : cb.equal(cb.lower(root.get("productBrand")), brand.toLowerCase());
    }

    public static Specification<Product> hasCategory(String category) {
        return (root, query, cb) -> category == null || category.isBlank()
                ? null
                : cb.equal(cb.lower(root.get("productCategory")), category.toLowerCase());
    }
}
