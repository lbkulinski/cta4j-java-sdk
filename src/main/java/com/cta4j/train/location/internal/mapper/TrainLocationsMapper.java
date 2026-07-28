package com.cta4j.train.location.internal.mapper;

import com.cta4j.train.common.internal.mapper.Qualifiers;
import com.cta4j.train.location.internal.wire.CtaLocationTrain;
import com.cta4j.train.location.internal.wire.CtaRoute;
import com.cta4j.train.location.model.LocationTrain;
import com.cta4j.train.location.model.TrainLocations;
import org.jetbrains.annotations.ApiStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(
    uses = Qualifiers.class,
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
@ApiStatus.Internal
public interface TrainLocationsMapper {
    TrainLocationsMapper INSTANCE = Mappers.getMapper(TrainLocationsMapper.class);

    @Mapping(target = "line", source = "name", qualifiedByName = "mapLine")
    @Mapping(target = "trains", source = "train")
    TrainLocations toDomain(CtaRoute route);

    @Mapping(target = "run", source = "rn")
    @Mapping(target = "destinationStationId", source = "destSt")
    @Mapping(target = "destinationName", source = "destNm")
    @Mapping(target = "direction", source = "trDr", qualifiedByName = "map15ToTrainDirection")
    @Mapping(target = "nextStationId", source = "nextStaId")
    @Mapping(target = "nextStopId", source = "nextStpId")
    @Mapping(target = "nextStationName", source = "nextStaNm")
    @Mapping(target = "predictionTime", source = "prdt", qualifiedByName = "mapTimestamp")
    @Mapping(target = "arrivalTime", source = "arrT", qualifiedByName = "mapTimestamp")
    @Mapping(target = "approaching", source = "isApp", qualifiedByName = "map01ToBoolean")
    @Mapping(target = "delayed", source = "isDly", qualifiedByName = "map01ToBoolean")
    @Mapping(target = "flags", source = "flags")
    @Mapping(target = "coordinates.latitude", source = "lat", qualifiedByName = "mapCoordinate")
    @Mapping(target = "coordinates.longitude", source = "lon", qualifiedByName = "mapCoordinate")
    @Mapping(target = "coordinates.heading", source = "heading", qualifiedByName = "mapHeading")
    LocationTrain toDomain(CtaLocationTrain train);
}
