package com.loofi.backoffice.controller;

import com.loofi.backoffice.service.MfsLedgerTransactionService;
import com.loofi.backoffice.entity.LedgerTransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ledger-transaction")
public class LedgerTransactionController {

    @Autowired
    private MfsLedgerTransactionService mfsLedgerTransactionService;

    @PostMapping
    LedgerTransaction saveMfsLedgerTransaction(@RequestBody LedgerTransaction ledgerTransaction){
        return mfsLedgerTransactionService.saveMfsLedgerTransaction(ledgerTransaction);
    }

    @PostMapping("/change/status")
    LedgerTransaction changeStatusMfsLedgerTransaction(@RequestBody LedgerTransaction ledgerTransaction){
        return mfsLedgerTransactionService.changeStatusMfsLedgerTransaction(ledgerTransaction);
    }

    @GetMapping
    List<LedgerTransaction> getListMfsLedgerTransaction(){
        return mfsLedgerTransactionService.getListMfsLedgerTransaction();
    }
}
