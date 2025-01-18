package com.senla.specification;

import com.senla.dto.cab.CabFilterDto;
import com.senla.model.cab.Cab;
import com.senla.model.cab.Cab_;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CabSpecification {

    public static Specification<Cab> isOnShift(Boolean isOnShift) {
        return (Root<Cab> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.equal(root.get(Cab_.isOnShift), isOnShift);
        };
    }

//    public static Specification<Cab> byTaxiCompanyName(String txName) {
//        return (Root<Cab> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
//            return cb.equal(root.get(Cab_.company).get(TaxiCompany_.name), txName);
//        };
//    }

    public static Specification<Cab> vinLikePattern(String vin) {
        return (Root<Cab> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.like(root.get(Cab_.vin), "%" + vin + "%");
        };
    }

//    Join<Rate, City> _j = root.join(Rate_.city);
//        cq.where(cb.like(_j.get(City_.cityName), "%ins%"));

    public static Specification<Cab> colorDescriptionLikePattern(String color) {
        return (Root<Cab> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.like(root.get(Cab_.colorDescription), "%" + color + "%");
        };
    }

    public static Specification<Cab> licensePlateLikePattern(String licensePlate) {
        return (Root<Cab> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.like(root.get(Cab_.licensePlate), "%" + licensePlate + "%");
        };
    }

    public static Specification<Cab> manufactureDateInterval(LocalDate dayFrom, LocalDate dayTo) {
        return (Root<Cab> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate dayFromPredicate = cb.conjunction();
            Predicate dayToPredicate = cb.conjunction();
            if(dayFrom != null) dayFromPredicate = cb.greaterThanOrEqualTo(root.get(Cab_.manufactureDate), dayFrom);
            if(dayTo   != null) dayToPredicate   = cb.lessThanOrEqualTo(root.get(Cab_.manufactureDate), dayTo);
            return cb.and(dayFromPredicate, dayToPredicate);
        };
    }


    public static Specification<Cab> buildSpecification(CabFilterDto filterDto) {
        Specification<Cab> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        if(filterDto.getManufactureDateMin() != null || filterDto.getManufactureDateMax() != null) {
            spec = spec.and(manufactureDateInterval(filterDto.getManufactureDateMin(), filterDto.getManufactureDateMax()));
        }
        if(filterDto.getVin() != null) {
            spec = spec.and(vinLikePattern(filterDto.getVin()));
        }
        if(filterDto.getIsOnShift() != null) {
            spec = spec.and(isOnShift(filterDto.getIsOnShift()));
        }
        if(filterDto.getLicensePlate() != null) {
            spec = spec.and(licensePlateLikePattern(filterDto.getLicensePlate()));
        }
        if(filterDto.getColorDescription() != null) {
            spec = spec.and(colorDescriptionLikePattern(filterDto.getColorDescription()));
        }

        return spec;
    }
}
