package com.microservices.lifecycle.bankstatement.repository;

import com.microservices.lifecycle.bankstatement.model.StatementRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StatementCustomRespository {

    public Page<StatementRecord> findAllSortedByDate(Pageable pageable);

}
