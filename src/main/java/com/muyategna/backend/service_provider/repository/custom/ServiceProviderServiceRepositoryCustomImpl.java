package com.muyategna.backend.service_provider.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ServiceProviderServiceRepositoryCustomImpl implements ServiceProviderServiceRepositoryCustom {

    private  final JPAQueryFactory jpaQueryFactory;

    public ServiceProviderServiceRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

//    @Override
//    public Set<ServiceProviderServiceWithDistanceFromJob> findServiceProviderServicesByServiceAndLocation(Long serviceId, Point geoPoint, Double radius) {
//        return Set.of();
//    }
//
//    @Override
//    public Set<ServiceProviderServiceWithDistanceFromJob> findServiceProviderServicesByServiceAndLocation(Long serviceId, Point geoPoint, Double radiusMeters, List<Long> excludedServiceProviderIds) {
//        return Set.of();
//    }


//    @Override // If this method is part of an interface
//    public Set<ServiceProviderServiceWithDistanceFromJob> findServiceProviderServicesByServiceAndLocation(
//            Long serviceId,
//            Point geoPoint,
//            Double radiusMeters
//    ) {
//        // Validate input parameters thoroughly
//        if (serviceId == null || geoPoint == null || radiusMeters == null || radiusMeters <= 0) {
//             log.warn("Invalid input for findServiceProviderServicesByServiceAndLocation: serviceId={}, geoPoint={}, radiusMeters={}", serviceId, geoPoint, radiusMeters);
//            return Collections.emptySet();
//        }
//
//        // Q-classes are typically imported statically for brevity
//         QServiceProviderService sps = QServiceProviderService.serviceProviderService;
//         QServiceProvider sp = QServiceProvider.serviceProvider;
//         QAddress addr = QAddress.address;
//         QService srv = QService.service;
//
//
//        // The `ST_Distance` function will return distance in meters for GEOGRAPHY types.
//        NumberExpression<Double> distanceExpression = Expressions.numberTemplate(Double.class,
//                "ST_Distance({0}, {1})", // PostGIS function
//                addr.geoPoint, // The GEOGRAPHY column from the Address entity
//                Expressions.constant(geoPoint)// The GEOGRAPHY point from the input
//        );
//
//        return jpaQueryFactory
//                .select(
//                        sps, // Select the full entity
//                        distanceExpression.as("distanceMeters") // Select the distance expression, aliased
//                )
//                .from(sps)
//                .distinct()
//                .join(sps.provider, sp)
//                .join(sp.businessAddress, addr)
//                .join(sps.service, srv)
//                .where(
//                        srv.id.eq(serviceId)
//                                .and(sps.isActive.isTrue())
//                                .and(sp.isActive.isTrue())
//                                .and(sp.isVerified.isTrue())
//                                .and(sp.isBlocked.isFalse())
//                                .and(sp.maxTravelDistanceInKM.multiply(1000).loe(radiusMeters).or(sp.maxTravelDistanceInKM.isNull()))
//                                .and(
//                                        Expressions.booleanTemplate(
//                                                "ST_DWithin({0}, {1}, {2})",
//                                                addr.geoPoint, // This needs to be a GEOGRAPHY type in DB
//                                                Expressions.constant(geoPoint), // JTS Point mapped to GEOGRAPHY
//                                                Expressions.constant(radiusMeters)
//                                        )
//                                )
//                )
//                .fetch()
//                .stream()
//                .map(tuple -> new ServiceProviderServiceWithDistanceFromJob(
//                        tuple.get(sps), // Get the ServiceProviderService entity
//                        tuple.get(distanceExpression) // Get by the expression itself or its alias if specified
//                )).collect(Collectors.toSet()); // Collect into a HashSet
//    }
//
//    @Override
//    public Set<ServiceProviderServiceWithDistanceFromJob> findServiceProviderServicesByServiceAndLocation(
//            Long serviceId,
//            Point geoPoint,
//            Double radiusMeters,
//            List<Long> excludedServiceProviderIds) {
//        // Validate input parameters thoroughly
//        if (serviceId == null || geoPoint == null || radiusMeters == null || radiusMeters <= 0) {
//            log.warn("Invalid input for findServiceProviderServicesByServiceAndLocation : serviceId={}, geoPoint={}, radiusMeters={}", serviceId, geoPoint, radiusMeters);
//            return Collections.emptySet();
//        }
//
//        // Q-classes are typically imported statically for brevity
//        QServiceProviderService sps = QServiceProviderService.serviceProviderService;
//        QServiceProvider sp = QServiceProvider.serviceProvider;
//        QAddress addr = QAddress.address;
//        QService srv = QService.service;
//
//
//        // The `ST_Distance` function will return distance in meters for GEOGRAPHY types.
//        NumberExpression<Double> distanceExpression = Expressions.numberTemplate(
//                Double.class,
//                "ST_Distance({0}, {1})", // PostGIS function
//                addr.geoPoint, // The GEOGRAPHY column from the Address entity
//                Expressions.constant(geoPoint)// The GEOGRAPHY point from the input
////                Expressions.constant(true)
//        );
//
//        return jpaQueryFactory
//                .select(
//                        sps, // Select the full entity
//                        distanceExpression.as("distanceMeters") // Select the distance expression, aliased
//                )
//                .from(sps)
//                .distinct()
//                .join(sps.provider, sp)
//                .join(sp.businessAddress, addr)
//                .join(sps.service, srv)
//                .where(
//                        srv.id.eq(serviceId)
//                                .and(sps.provider.id.notIn(excludedServiceProviderIds)) // Exclude specified service providers
//                                .and(sps.isActive.isTrue())
//                                .and(sp.isActive.isTrue())
//                                .and(sp.isVerified.isTrue())
//                                .and(sp.isBlocked.isFalse())
//                                .and(sp.maxTravelDistanceInKM.multiply(1000).loe(radiusMeters).or(sp.maxTravelDistanceInKM.isNull()))
//                                .and(
//                                        Expressions.booleanTemplate(
//                                                "ST_DWithin({0}, {1}, {2})",
//                                                addr.geoPoint, // This needs to be a GEOGRAPHY type in DB
//                                                Expressions.constant(geoPoint), // JTS Point mapped to GEOGRAPHY
//                                                Expressions.constant(radiusMeters)
//                                        )
//                                )
//                )
//                .fetch()
//                .stream()
//                .map(tuple -> new ServiceProviderServiceWithDistanceFromJob(
//                        tuple.get(sps), // Get the ServiceProviderService entity
//                        tuple.get(distanceExpression) // Get by the expression itself or its alias if specified
//                )).collect(Collectors.toSet()); // Collect into a HashSet
//    }






}
