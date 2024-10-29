package com.interview.test.service;

import com.interview.test.entity.Account;
import com.interview.test.entity.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AccountTransferServiceTest {

    private Account debitAccount;

    private Account creditAccount;

    private AccountTransferService accountTransferService;

    @BeforeEach
    void setUp(){
        debitAccount = new Account("act-123", new Money(100));
        creditAccount = new Account("act-124", new Money(200));
        accountTransferService = new AccountTransferService();
    }

    @Test
    @DisplayName("test account transfer service flow")
    void testAccountTransfer(){
        Money debitAmount = new Money(50);
        accountTransferService.transfer(debitAccount,creditAccount,debitAmount);

        assert debitAccount.getBalanceValue() == 50;
        assert creditAccount.getBalanceValue() == 250;
    }

    @Test
    @DisplayName("test account transfer service flow with multiple times")
    void testAccountTransferLoad(){
        Money debitAmount = new Money(10);
        Money creditAmount = new Money(2);
        for(int i=0;i<10;i++){
            accountTransferService.transfer(debitAccount,creditAccount,debitAmount);
            accountTransferService.transfer(creditAccount,debitAccount,creditAmount);
        }

        assert debitAccount.getBalanceValue() == 20;
        assert creditAccount.getBalanceValue() == 280;
    }

    @Test
    @DisplayName("test account transfer service flow with multiple threads and multiple credit and debit")
    void testAccountTransferWithMultipleCreditAndDebit() throws InterruptedException {
        Money debitAmount = new Money(10);
        Money creditAmount = new Money(5);

        ExecutorService service = Executors.newFixedThreadPool(10);
        for(int i=0;i<10;i++){
            service.submit(()->accountTransferService.transfer(debitAccount,creditAccount,debitAmount));
            service.submit(()->accountTransferService.transfer(creditAccount,debitAccount,creditAmount));
        }
        service.awaitTermination(5, TimeUnit.SECONDS);

        assert debitAccount.getBalanceValue() == 50;
        assert creditAccount.getBalanceValue() == 250;
    }
}
