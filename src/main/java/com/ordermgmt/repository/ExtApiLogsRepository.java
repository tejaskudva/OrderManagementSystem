package com.ordermgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ordermgmt.models.entity.ExternalApiLogs;

@Repository
public interface ExtApiLogsRepository extends JpaRepository<ExternalApiLogs, Long> {

}
