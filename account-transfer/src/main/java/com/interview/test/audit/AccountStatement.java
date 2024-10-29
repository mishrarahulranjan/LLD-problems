package com.interview.test.audit;

import com.interview.test.entity.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountStatement {

    private static Map<Account, List<AccountTransactionAudit>> statementCache = new HashMap<>();

    public static void addStatement(Account account ,AccountTransactionAudit audit){

        statementCache.compute(account,(key,value)->{
            if(value==null){
                value = new ArrayList<>();
            }
            value.add(audit);
            return value;
        });

    }

    public static List<AccountTransactionAudit> getStatement(Account account){
        return statementCache.get(account);
    }


}
