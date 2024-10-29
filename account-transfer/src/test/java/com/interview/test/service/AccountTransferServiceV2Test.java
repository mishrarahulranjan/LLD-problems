package com.interview.test.service;

import com.interview.test.concurrent.entity.Account;
import com.interview.test.concurrent.entity.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AccountTransferServiceV2Test {

    private Account debitAccount;

    private Account creditAccount;

    private AccountTransferServiceV2 accountTransferService;

    @BeforeEach
    void setUp(){
        debitAccount = new Account("act-123", new Money(100));
        creditAccount = new Account("act-124", new Money(200));
        accountTransferService = new AccountTransferServiceV2();
    }

    @Test
    @DisplayName("test account transfer service flow with multiple threads and multiple credit and debit")
    void testAccountTransferWithMultipleCreditAndDebit() throws InterruptedException {
        Money debitAmount = new Money(10);
        Money creditAmount = new Money(1);

        ExecutorService service = Executors.newFixedThreadPool(10);
        for(int i=0;i<10;i++){
            service.submit(()->accountTransferService.transfer(creditAccount,debitAccount,creditAmount));
            service.submit(()->accountTransferService.transfer(debitAccount,creditAccount,debitAmount));
        }
        service.awaitTermination(5, TimeUnit.SECONDS);

        assert debitAccount.getBalanceValue() == 10;
        assert creditAccount.getBalanceValue() == 290;
    }
}
