module org.goodiemania.nzcp4j {
    requires java.net.http;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.cbor;
    requires org.bouncycastle.provider;

    exports org.goodiemania.nzcp4j.impl.dto to com.fasterxml.jackson.databind;
    opens org.goodiemania.nzcp4j.impl.dto to com.fasterxml.jackson.databind;

    exports org.goodiemania.nzcp4j;
    exports org.goodiemania.nzcp4j.exceptions;
    opens org.goodiemania.nzcp4j;
    opens org.goodiemania.nzcp4j.exceptions;
}
