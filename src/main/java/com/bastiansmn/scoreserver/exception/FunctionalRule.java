package com.bastiansmn.scoreserver.exception;

import lombok.Getter;

@Getter
public enum FunctionalRule {

    NS_NOT_FOUND("NS_NOT_FOUND", "Namespace not found"),
    NS_ALREADY_EXISTS("NS_ALREADY_EXISTS", "Namespace already exists"),
    NS_UNAUTHORIZED("NS_UNAUTHORIZED", "Unauthorized access to namespace"),
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found"),
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "User already exists"),
    SCORE_NOT_FOUND("SCORE_NOT_FOUND", "Score not found")
    ;

    private final String name;
    private final String message;
    FunctionalRule(final String name, final String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.getName(), this.getMessage());
    }

    public String toString(final Object... params) {
        return String.format("%s - %s", this.getName(), String.format(this.getMessage(), params));
    }

}
