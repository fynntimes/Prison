/**
 * (C) 2014 SirFaizdat
 */
package me.sirfaizdat.prison.core;

/**
 * @author SirFaizdat
 */
public class FailedToStartException extends Exception {
    private static final long serialVersionUID = 1L;

    public FailedToStartException(String message) {
        super(message);
    }

}
