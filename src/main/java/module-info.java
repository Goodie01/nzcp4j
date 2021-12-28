module org.goodiemania.nzcp4j {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.cbor;
    requires unirest.java;
    requires commons.codec;
    requires org.bouncycastle.provider;

    exports org.goodiemania.nzcp4j;
    opens org.goodiemania.nzcp4j;
    exports org.goodiemania.nzcp4j.impl.dto to com.fasterxml.jackson.databind;
    opens org.goodiemania.nzcp4j.impl.dto to com.fasterxml.jackson.databind;
}