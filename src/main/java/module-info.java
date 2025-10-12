/**
 * The cta4j Java SDK module. Provides client APIs for CTA bus and train data.
 */
module cta4j.java.sdk {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;

    exports com.cta4j.client;
    exports com.cta4j.exception;
    exports com.cta4j.model.bus;
    exports com.cta4j.model.train;
}
