//package com.muyategna.backend.service_provider.repository.projection;
//
//import com.muyategna.backend.service_provider.entity.ServiceProviderService;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.Objects;
//
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//public class ServiceProviderServiceWithDistanceFromJob {
//    private ServiceProviderService serviceProviderService;
//    private Double distanceMeters;
//
//    // Override equals() and hashCode() if you put this in a Set
//    // based on the serviceProviderService's ID
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ServiceProviderServiceWithDistanceFromJob that = (ServiceProviderServiceWithDistanceFromJob) o;
//        return Objects.equals(serviceProviderService, that.serviceProviderService);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(serviceProviderService);
//    }
//
//    public ServiceProviderServiceWithDistanceFromJob convertFromObjectArray(Object[] objectArray) {
//        if (objectArray == null || objectArray.length < 2) {
//            return null;
//        }
//        this.serviceProviderService = (ServiceProviderService) objectArray[0];
//        this.distanceMeters = (Double) objectArray[1];
//        return this;
//    }
//}