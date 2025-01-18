package com.senla.specification;

import com.senla.dto.ride.RideFilterDto;
import com.senla.model.ride.Ride;

import com.senla.model.ride.Ride_;
import com.senla.types.RideStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class RideSpecification {


    public static Specification<Ride> createdAtDateInterval(LocalDateTime vFrom, LocalDateTime vTo) {
        return (Root<Ride> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate vFromPredicate = cb.conjunction();
            Predicate vToPredicate = cb.conjunction();
            if(vFrom != null) vFromPredicate = cb.greaterThanOrEqualTo(root.get(Ride_.createdAt), vFrom);
            if(vTo   != null) vToPredicate   = cb.lessThanOrEqualTo(root.get(Ride_.createdAt), vTo);
            return cb.and(vFromPredicate, vToPredicate);
        };
    }

    public static Specification<Ride> rideAcceptedDateInterval(LocalDateTime vFrom, LocalDateTime vTo) {
        return (Root<Ride> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate vFromPredicate = cb.conjunction();
            Predicate vToPredicate = cb.conjunction();
            if(vFrom != null) vFromPredicate = cb.greaterThanOrEqualTo(root.get(Ride_.rideAcceptedAt), vFrom);
            if(vTo   != null) vToPredicate   = cb.lessThanOrEqualTo(root.get(Ride_.rideAcceptedAt), vTo);
            return cb.and(vFromPredicate, vToPredicate);
        };
    }

    public static Specification<Ride> rideDriverWaitingInterval(LocalDateTime vFrom, LocalDateTime vTo) {
        return (Root<Ride> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate vFromPredicate = cb.conjunction();
            Predicate vToPredicate = cb.conjunction();
            if(vFrom != null) vFromPredicate = cb.greaterThanOrEqualTo(root.get(Ride_.rideDriverWaiting), vFrom);
            if(vTo   != null) vToPredicate   = cb.lessThanOrEqualTo(root.get(Ride_.rideDriverWaiting), vTo);
            return cb.and(vFromPredicate, vToPredicate);
        };
    }

    public static Specification<Ride> rideStartTimeInterval(LocalDateTime vFrom, LocalDateTime vTo) {
        return (Root<Ride> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate vFromPredicate = cb.conjunction();
            Predicate vToPredicate = cb.conjunction();
            if(vFrom != null) vFromPredicate = cb.greaterThanOrEqualTo(root.get(Ride_.rideStartTime), vFrom);
            if(vTo   != null) vToPredicate   = cb.lessThanOrEqualTo(root.get(Ride_.rideStartTime), vTo);
            return cb.and(vFromPredicate, vToPredicate);
        };
    }

    public static Specification<Ride> rideEndTimeInterval(LocalDateTime vFrom, LocalDateTime vTo) {
        return (Root<Ride> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate vFromPredicate = cb.conjunction();
            Predicate vToPredicate = cb.conjunction();
            if(vFrom != null) vFromPredicate = cb.greaterThanOrEqualTo(root.get(Ride_.rideEndTime), vFrom);
            if(vTo   != null) vToPredicate   = cb.lessThanOrEqualTo(root.get(Ride_.rideEndTime), vTo);
            return cb.and(vFromPredicate, vToPredicate);
        };
    }

    public static Specification<Ride> rideStatusExact(RideStatus rideStatus) {
        return (Root<Ride> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.equal(root.get(Ride_.status), rideStatus);
        };
    }


    public static Specification<Ride> rideActualPriceInterval(BigDecimal vFrom, BigDecimal vTo) {
        return (Root<Ride> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate vFromPredicate = cb.conjunction();
            Predicate vToPredicate = cb.conjunction();
            if(vFrom != null) vFromPredicate = cb.greaterThanOrEqualTo(root.get(Ride_.rideActualPrice), vFrom);
            if(vTo   != null) vToPredicate   = cb.lessThanOrEqualTo(root.get(Ride_.rideActualPrice), vTo);
            return cb.and(vFromPredicate, vToPredicate);
        };
    }

    public static Specification<Ride> rideDistanceActualInterval(BigDecimal vFrom, BigDecimal vTo) {
        return (Root<Ride> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate vFromPredicate = cb.conjunction();
            Predicate vToPredicate = cb.conjunction();
            if(vFrom != null) vFromPredicate = cb.greaterThanOrEqualTo(root.get(Ride_.rideDistanceActualMeters), vFrom);
            if(vTo   != null) vToPredicate   = cb.lessThanOrEqualTo(root.get(Ride_.rideDistanceActualMeters), vTo);
            return cb.and(vFromPredicate, vToPredicate);
        };
    }

    public static Specification<Ride> rideDistanceExpectedInterval(BigDecimal vFrom, BigDecimal vTo) {
        return (Root<Ride> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate vFromPredicate = cb.conjunction();
            Predicate vToPredicate = cb.conjunction();
            if(vFrom != null) vFromPredicate = cb.greaterThanOrEqualTo(root.get(Ride_.rideDistanceExpectedMeters), vFrom);
            if(vTo   != null) vToPredicate   = cb.lessThanOrEqualTo(root.get(Ride_.rideDistanceExpectedMeters), vTo);
            return cb.and(vFromPredicate, vToPredicate);
        };
    }

    public static Specification<Ride> rideTipInterval(BigDecimal vFrom, BigDecimal vTo) {
        return (Root<Ride> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate vFromPredicate = cb.conjunction();
            Predicate vToPredicate = cb.conjunction();
            if(vFrom != null) vFromPredicate = cb.greaterThanOrEqualTo(root.get(Ride_.rideTip), vFrom);
            if(vTo   != null) vToPredicate   = cb.lessThanOrEqualTo(root.get(Ride_.rideTip), vTo);
            return cb.and(vFromPredicate, vToPredicate);
        };
    }

    public static Specification<Ride> buildSpecification(RideFilterDto filterDto) {
        Specification<Ride> spec = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        if (filterDto.getRideTipMin() != null || filterDto.getRideTipMax() != null) {
            spec = spec.and(RideSpecification.rideTipInterval(filterDto.getRideTipMin(), filterDto.getRideTipMax()));
        }
        if (filterDto.getRideActualPriceMin() != null || filterDto.getRideActualPriceMax() != null) {
            spec = spec.and(RideSpecification.rideActualPriceInterval(filterDto.getRideActualPriceMin(), filterDto.getRideActualPriceMax()));
        }
        if (filterDto.getRideDistanceExpectedMin() != null || filterDto.getRideDistanceExpectedMax() != null) {
            spec = spec.and(RideSpecification.rideDistanceExpectedInterval(filterDto.getRideDistanceExpectedMin(), filterDto.getRideDistanceExpectedMax()));
        }
        if (filterDto.getRideDistanceActualMin() != null || filterDto.getRideDistanceActualMax() != null) {
            spec = spec.and(RideSpecification.rideDistanceActualInterval(filterDto.getRideDistanceActualMin(), filterDto.getRideDistanceActualMax()));
        }

        if (filterDto.getRideStatus() != null) {
            spec = spec.and(RideSpecification.rideStatusExact(filterDto.getRideStatus()));
        }

        if (filterDto.getCreatedAtMin() != null || filterDto.getCreatedAtMax() != null) {
            spec = spec.and(RideSpecification.createdAtDateInterval(filterDto.getCreatedAtMin(), filterDto.getCreatedAtMax()));
        }
        if (filterDto.getRideAcceptedAtMin() != null || filterDto.getRideAcceptedAtMax() != null) {
            spec = spec.and(RideSpecification.rideAcceptedDateInterval(filterDto.getRideAcceptedAtMin(), filterDto.getRideAcceptedAtMax()));
        }

        if (filterDto.getRideDriverWaitingMin() != null || filterDto.getRideDriverWaitingMax() != null) {
            spec = spec.and(RideSpecification.rideDriverWaitingInterval(filterDto.getRideDriverWaitingMin(), filterDto.getRideDriverWaitingMax()));
        }

        if (filterDto.getRideStartTimeMin() != null || filterDto.getRideStartTimeMax() != null) {
            spec = spec.and(RideSpecification.rideStartTimeInterval(filterDto.getRideStartTimeMin(), filterDto.getRideStartTimeMax()));
        }
        if (filterDto.getRideEndTimeMin() != null || filterDto.getRideEndTimeMax() != null) {
            spec = spec.and(RideSpecification.rideEndTimeInterval(filterDto.getRideEndTimeMin(), filterDto.getRideEndTimeMax()));
        }

        return spec;
    }
}
