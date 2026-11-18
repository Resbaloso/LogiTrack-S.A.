package com.logitracksa.logitracksaapi.exception;

public class DatabaseRuleException extends RuntimeException{
    public DatabaseRuleException(String mensaje){
        super(mensaje);
    }
}
