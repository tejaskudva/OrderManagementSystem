package com.ordermgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ordermgmt.models.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	@Transactional
	@Modifying
	@Query("UPDATE Order o SET o.status = :status WHERE o.id = :id")
	int updateOrderStatus(@Param("id") Long id, @Param("status") String status);

}
