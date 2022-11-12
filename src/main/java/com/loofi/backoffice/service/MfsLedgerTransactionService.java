package com.loofi.backoffice.service;

import com.loofi.backoffice.constants.MfsProductErrors;
import com.loofi.backoffice.entity.LedgerTransaction;
import com.loofi.backoffice.entity.Transaction;
import com.loofi.backoffice.entity.TransactionData;
import com.loofi.backoffice.entity.TransactionResponse;
import com.loofi.backoffice.entity.TransactionType;
import com.loofi.backoffice.exceptions.CommonException;
import com.loofi.backoffice.repository.MfsLedgerTransactionRepository;
import com.loofi.backoffice.repository.UserRepositoty;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MfsLedgerTransactionService {

  @Autowired
  private MfsLedgerTransactionRepository mfsLedgerTransactionRepository;

  @Autowired
  private UserRepositoty userRepositoty;

  private static RestTemplate restTemplate = new RestTemplate();
  @Value("${core.base.url:http://103.125.252.81:8080/account/}")
  private String coreBaseUrl;
  private final String ACCOUNT_NUMBER = "10100";

  public LedgerTransaction saveMfsLedgerTransaction(LedgerTransaction ledgerTransaction) {
    System.out.println(ledgerTransaction.toString());
    if (ledgerTransaction.getTransactionType() == com.loofi.backoffice.enums.TransactionType.BankCashIn) {
      ledgerTransaction.setFromAC(ACCOUNT_NUMBER);
    } else if (ledgerTransaction.getTransactionType() == com.loofi.backoffice.enums.TransactionType.BankCashOut) {
      ledgerTransaction.setToAc(ACCOUNT_NUMBER);
    }
    return mfsLedgerTransactionRepository.save(ledgerTransaction);
  }

  public List<LedgerTransaction> getListMfsLedgerTransaction() {
    List<LedgerTransaction> ledgerTransactionList = mfsLedgerTransactionRepository.findAll();
    Collections.sort(ledgerTransactionList, new sortCompare());
    return ledgerTransactionList;
  }


  public LedgerTransaction changeStatusMfsLedgerTransaction(LedgerTransaction reqmfsLedgerTransaction) {

    if (mfsLedgerTransactionRepository.existsById(reqmfsLedgerTransaction.getId())) {
      LedgerTransaction ledgerTransaction = mfsLedgerTransactionRepository.findById(reqmfsLedgerTransaction.getId()).get();
      String makerName, checkerName;
      checkerName = userRepositoty.findById(reqmfsLedgerTransaction.getChecker_id()).get().getUsername();
      makerName = userRepositoty.findById(ledgerTransaction.getMaker_id()).get().getUsername();
      TransactionResponse transactionResponse = doTransaction(ledgerTransaction, checkerName, makerName);
      ledgerTransaction.setStatus(transactionResponse.getStatus());
      ledgerTransaction.setTxnId(transactionResponse.getTxnId());
      ledgerTransaction.setChecker_id(reqmfsLedgerTransaction.getChecker_id());
      mfsLedgerTransactionRepository.save(ledgerTransaction);
      return ledgerTransaction;
    }
    throw new CommonException(
      MfsProductErrors.getErrorCode(MfsProductErrors.MFSPRODUCT_MANAGEMENT, MfsProductErrors.MFS_LEDGER_TRANSACTION_NOT_FOUND),
      MfsProductErrors.ERROR_MAP.get(MfsProductErrors.MFS_LEDGER_TRANSACTION_NOT_FOUND));
  }

  private TransactionResponse doTransaction(LedgerTransaction ledgerTransaction, String checkerName, String makerName) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    TransactionData transactionData = new TransactionData("884848", ledgerTransaction.getTransactionType().toString());
    TransactionType transactionType = new TransactionType(104);
    Transaction transaction = new Transaction();
    transaction.setAmount(ledgerTransaction.getAmount());
    transaction.setChannel("REST");
    transaction.setDescription("Distributor bank cash in");
    transaction.setMaker(makerName);
    transaction.setChecker(checkerName);
    transaction.setFromAc(ledgerTransaction.getFromAC());
    transaction.setToAc(ledgerTransaction.getToAc());
    transaction.setTag("BackOffice Txn");
    transaction.setNote(ledgerTransaction.getNote());
    transaction.setReferenceId(ledgerTransaction.getReference());
    transaction.setRequestId(UUID.randomUUID().toString());
    transaction.setTransactionType(transactionType);
    transaction.setData(transactionData);

    HttpEntity<Object> requestEntity = new HttpEntity<>(transaction, headers);
    ResponseEntity<TransactionResponse> responseUser = restTemplate.exchange(coreBaseUrl + "api/v1/txn",
      HttpMethod.POST,
      requestEntity,
      TransactionResponse.class);
    TransactionResponse transactionResponse = responseUser.getBody();
    return transactionResponse;
  }
}

class sortCompare implements Comparator<LedgerTransaction> {
  @Override
  public int compare(LedgerTransaction a, LedgerTransaction b) {
    return b.getTxnTime().compareTo(a.getTxnTime());
  }
}
