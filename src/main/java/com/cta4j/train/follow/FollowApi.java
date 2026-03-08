package com.cta4j.train.follow;

import com.cta4j.train.follow.model.FollowResponse;
import org.jspecify.annotations.NullMarked;

/**
 * Provides access to follow-related endpoints of the CTA Train Tracker API.
 * <p>
 * This API allows retrieval of information about a specific train run.
 */
@NullMarked
public interface FollowApi {
    /**
     * Retrieves information about a specific train run.
     *
     * @param run the run ID of the train to follow
     * @return a {@link FollowResponse} containing information about the specified train run
     * @throws NullPointerException if {@code run} is {@code null}
     */
    FollowResponse followTrain(String run);
}
