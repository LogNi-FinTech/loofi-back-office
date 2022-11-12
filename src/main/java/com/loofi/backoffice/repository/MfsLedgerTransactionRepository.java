package com.loofi.backoffice.repository;

import com.loofi.backoffice.entity.LedgerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MfsLedgerTransactionRepository extends JpaRepository<LedgerTransaction, Integer> {
}
