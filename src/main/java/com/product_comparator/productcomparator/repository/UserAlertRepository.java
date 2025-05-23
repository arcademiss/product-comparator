package com.product_comparator.productcomparator.repository;

import com.product_comparator.productcomparator.entity.UserAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAlertRepository extends JpaRepository<UserAlert, Integer> {

}
