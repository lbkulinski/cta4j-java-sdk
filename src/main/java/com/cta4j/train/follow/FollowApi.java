package com.cta4j.train.follow;

import com.cta4j.train.follow.exception.Cta4jFollowException;
import com.cta4j.train.follow.model.FollowTrain;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

/**
 * Provides access to follow-related endpoints of the CTA Train Tracker API.
 * <p>
 * This API allows retrieval of information about a specific train run.
 */
@NullMarked
public interface FollowApi {
    /**
     * Retrieves a train by its run number.
     *
     * @param run the run number of the train
     * @return an {@link Optional} containing the {@link FollowTrain} if found, or an empty {@link Optional} if no
     * train is found for the given run number
     * @throws NullPointerException if {@code run} is {@code null}
     * @throws Cta4jFollowException if the API returns an error response or the response cannot be parsed
     */
    Optional<FollowTrain> findByRun(String run);
}
