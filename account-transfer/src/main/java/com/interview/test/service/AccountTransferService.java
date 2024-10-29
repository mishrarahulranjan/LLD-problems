package com.interview.test.service;

import com.interview.test.audit.AccountStatement;
import com.interview.test.audit.AccountTransactionAudit;
import com.interview.test.audit.TransactionStatus;
import com.interview.test.entity.Account;
import com.interview.test.entity.Money;

public class AccountTransferService {

    public void transfer(Account debitAccount, Account creditAccount, Money money){

        Account firstAccount = null;
        Account secondAccount = null;
        if(debitAccount.hashCode()< creditAccount.hashCode()){
            firstAccount= debitAccount;
            secondAccount= creditAccount;
        }else{
            firstAccount = creditAccount;
            secondAccount = debitAccount;
        }

         synchronized (firstAccount){
            synchronized (secondAccount){
                try{
                    debitAccount.debit(money);
                    creditAccount.credit(money);
                    AccountStatement.addStatement(debitAccount,
                            new AccountTransactionAudit("debited amount", TransactionStatus.SUCCESSS));
                    AccountStatement.addStatement(creditAccount,
                            new AccountTransactionAudit("credited amount", TransactionStatus.SUCCESSS));
                }catch (RuntimeException ex){
                    AccountStatement.addStatement(debitAccount,
                            new AccountTransactionAudit(ex.getMessage(), TransactionStatus.FAILURE));
                }

            }
        }
    }
}
