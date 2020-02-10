package com.microservices.lifecycle.bankstatement.repository;

import com.microservices.lifecycle.bankstatement.model.StatementRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementRecordRepository extends JpaRepository<StatementRecord, Long>, StatementCustomRespository { }
