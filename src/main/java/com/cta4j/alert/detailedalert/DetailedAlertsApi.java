package com.cta4j.alert.detailedalert;

import com.cta4j.alert.detailedalert.model.Alert;
import com.cta4j.alert.detailedalert.query.AlertsQuery;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public interface DetailedAlertsApi {
    List<Alert> list(AlertsQuery query);

    default List<Alert> list() {
        AlertsQuery query = AlertsQuery.builder()
                                       .build();

        return this.list(query);
    }
}
