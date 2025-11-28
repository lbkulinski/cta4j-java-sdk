/**
 * The cta4j Java SDK module. Provides client APIs for CTA bus and train data.
 */
module cta4j.java.sdk {
    requires tools.jackson.databind;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.slf4j;
    requires static org.jetbrains.annotations;

    exports com.cta4j.bus.client;
    exports com.cta4j.train.client;
    exports com.cta4j.bus.model;
    exports com.cta4j.train.model;
    exports com.cta4j.exception;

    opens com.cta4j.bus.external.detour to tools.jackson.databind;
    opens com.cta4j.bus.external.direction to tools.jackson.databind;
    opens com.cta4j.bus.external.prediction to tools.jackson.databind;
    opens com.cta4j.bus.external.route to tools.jackson.databind;
    opens com.cta4j.bus.external.stop to tools.jackson.databind;
    opens com.cta4j.bus.external.vehicle to tools.jackson.databind;
    opens com.cta4j.train.external.arrival to tools.jackson.databind;
    opens com.cta4j.train.external.follow to tools.jackson.databind;
}
