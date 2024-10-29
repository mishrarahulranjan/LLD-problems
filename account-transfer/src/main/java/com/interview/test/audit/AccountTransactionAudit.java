package com.interview.test.audit;

import java.util.Date;

public class AccountTransactionAudit {

    private Date timeStamped;

    private String message;

    private TransactionStatus status;

    public AccountTransactionAudit(String message, TransactionStatus status){
        this.message = message;
        this.status = status;
        this.timeStamped = new Date();
    }

    @Override
    public String toString() {
        return "AccountTransactionAudit{" +
                "timeStamped=" + timeStamped +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
