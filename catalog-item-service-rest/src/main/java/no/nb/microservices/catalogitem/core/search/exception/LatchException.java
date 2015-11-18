package no.nb.microservices.catalogitem.core.search.exception;

public class LatchException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LatchException(Exception ex) {
        super(ex);
    }

}
