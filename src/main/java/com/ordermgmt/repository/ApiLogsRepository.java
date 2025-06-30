package com.ordermgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ordermgmt.models.entity.ApiLogs;

@Repository
public interface ApiLogsRepository extends JpaRepository<ApiLogs, Long> {

}
