package com.microservices.lifecycle.bankstatement.service;

import com.microservices.lifecycle.bankstatement.model.StatementRecord;
import com.microservices.lifecycle.bankstatement.repository.StatementRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class StatementService {

    private final StatementRecordRepository repository;

    public StatementService(StatementRecordRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<StatementRecord> findAllSorted(Pageable pageable) {
        Page<StatementRecord> result = repository.findAllSortedByDate(pageable);
        return result;
    }


    public StatementRecord save(StatementRecord product) {
        product.setDate(LocalDateTime.now());

        return repository.save(product);
    }

}
