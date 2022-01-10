package com.muaz.cryptoExchange.exception;

public class BlockChainServiceNotRespondingException extends Exception {

    private static final long serialVersionUID = 3159011240160233998L;
    public static final String UNAVAILABLE = " is presently unavailable";

    public BlockChainServiceNotRespondingException(String serviceName) {
        super(new StringBuilder(serviceName).append(UNAVAILABLE).toString());
    }

    public String toString() {
        return "External Service error. Please try again.";
    }

}
