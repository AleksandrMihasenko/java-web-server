package org.http;

public enum HttpStatusCode {
    // Client errors
    CLIENT_ERROR_400_BAD_REQUEST(400, "Bad Request"),
    CLIENT_ERROR_401_METHOD_NOT_ALLOWED(401, "Method Not Allowed"),
    CLIENT_ERROR_414_BAD_REQUEST(414, "URI Too Long"),

    // Client errors
    CLIENT_ERROR_500_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    CLIENT_ERROR_501_NOT_IMPLEMENTED(501, "Not Implemented");

    public final int STATUS_CODE;
    public final String MESSAGE;

    HttpStatusCode(int STATUS_CODE, String MESSAGE) {
        this.STATUS_CODE = STATUS_CODE;
        this.MESSAGE = MESSAGE;
    }
}