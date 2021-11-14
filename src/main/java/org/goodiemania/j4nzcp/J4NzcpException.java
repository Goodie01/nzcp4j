package org.goodiemania.j4nzcp;

public abstract class J4NzcpException extends Exception{
    private String url;

    public J4NzcpException(final String message, final String Url) {
        super(message);
        url = Url;
    }

    public J4NzcpException(final String message) {
        super(message);
    }


    /**
     * @return Nullable URL that points to the location in the spec that this is from
     */
    public String getUrl() {
        return url;
    }
}
