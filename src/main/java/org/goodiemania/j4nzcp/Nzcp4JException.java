package org.goodiemania.j4nzcp;

public abstract class Nzcp4JException extends Exception{
    private String url;

    public Nzcp4JException(final String message, final String url) {
        super(message);
        this.url = url;
    }

    public Nzcp4JException(String message, final String url, Throwable cause) {
        super(message, cause);
        this.url = url;
    }

    public Nzcp4JException(String message, Throwable cause) {
        super(message, cause);
    }

    public Nzcp4JException(final String message) {
        super(message);
    }


    /**
     * @return Nullable URL that points to the location in the spec that this is from
     */
    public String getUrl() {
        return url;
    }
}
