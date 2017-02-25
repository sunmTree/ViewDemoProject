package com.sm.libs.scene;

/**
 * Created by sm on 17-2-25.
 */

public class ShouldNotReachHereException extends RuntimeException {

    private static final long serialVersionUID = 3557227234502703921L;

    public ShouldNotReachHereException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ShouldNotReachHereException(String detailMessage) {
        super(detailMessage);
    }

    public String toString() {
        return "ShouldNotReachHereException [reason=" + this.getMessage() + ", cause=" + this.getCause() + "]";
    }

}
