package com.microservices.lifecycle.bankstatement.repository;

import com.microservices.lifecycle.bankstatement.model.StatementRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class StatementRecordRepositoryImpl implements StatementCustomRespository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Page<StatementRecord> findAllSortedByDate(Pageable pageable) {
        TypedQuery<StatementRecord> query = entityManager.createNamedQuery("sortedByDate", StatementRecord.class);

        List<StatementRecord> list = query.getResultList();

        if (list.isEmpty()) {
            return Page.empty();
        }

        int totalRows = list.size();

        System.out.println(totalRows + " banking record(s) found!");

        Page<StatementRecord> result = new PageImpl<StatementRecord>(list, pageable, totalRows);

        return result;
    }
}
