package com.interview.test.service;

import com.interview.test.concurrent.entity.Account;
import com.interview.test.concurrent.entity.Money;

import java.util.concurrent.locks.Lock;

public class AccountTransferServiceV2 {

    public void transfer(Account debitAccount, Account creditAccount, Money money){

        Lock firstLock = null;
        Lock secondLock  = null;
        if(debitAccount.hashCode()< creditAccount.hashCode()){
            firstLock = debitAccount.getLock();
            secondLock = creditAccount.getLock();
        }else{
            firstLock  = creditAccount.getLock();
            secondLock  = debitAccount.getLock();
        }

        try{
            firstLock.lock();
            secondLock.lock();
            debitAccount.debit(money);
            creditAccount.credit(money);
        }finally {
            firstLock.unlock();
            secondLock.unlock();
        }
    }
}
