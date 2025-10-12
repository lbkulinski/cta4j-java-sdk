/**
 * The cta4j Java SDK module. Provides client APIs for CTA bus and train data.
 */
module cta4j.java.sdk {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.slf4j;

    exports com.cta4j.client;
    exports com.cta4j.exception;
    exports com.cta4j.model.bus;
    exports com.cta4j.model.train;
    exports com.cta4j.external.bus.detour to com.fasterxml.jackson.databind;
    exports com.cta4j.external.bus.direction to com.fasterxml.jackson.databind;
    exports com.cta4j.external.bus.prediction to com.fasterxml.jackson.databind;
    exports com.cta4j.external.bus.route to com.fasterxml.jackson.databind;
    exports com.cta4j.external.bus.stop to com.fasterxml.jackson.databind;
    exports com.cta4j.external.bus.vehicle to com.fasterxml.jackson.databind;
    exports com.cta4j.external.train.arrival to com.fasterxml.jackson.databind;
    exports com.cta4j.external.train.follow to com.fasterxml.jackson.databind;
}
