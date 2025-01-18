package com.senla.dao.ride;


import com.senla.dto.pagination.PaginationDetails;
import com.senla.model.driver.Driver_;
import com.senla.model.ride.Ride;

import com.senla.model.ride.Ride_;
import com.senla.model.shift.Shift;
import com.senla.model.shift.Shift_;
import com.senla.model.user.User_;
import com.senla.types.MatchRequestStatus;
import com.senla.util.dao.AbstractLongDao;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RideDao extends AbstractLongDao<Ride> implements IRideDao {

    @Override
    @PostConstruct
    protected void init() {
        super.clazz = Ride.class;
    }


    @Override
    public List<Ride> findActiveRide(Long customerId){
        TypedQuery<Ride> q = em.createQuery("""
                                            select r from Ride r
                                            where r.rideEndTime is null
                                                and r.customer.id = :customerId
                                            """, Ride.class);
        q.setParameter("customerId", customerId);
        return q.getResultList();
    }

    @Override
    public Double getMinimalCartesianDistance(Point startPoint, Point endPoint) {
        TypedQuery<Double> q = em.createQuery("""
                                            select st_distance( st_transform(:startPoint, 3857), st_transform(:endPoint, 3857))
                                            """, Double.class);
        q.setParameter("startPoint", startPoint);
        q.setParameter("endPoint", endPoint);
        return q.getSingleResult();
    }

    @Override
    public Optional<Ride> matchRideAndShift(Long rideId, Long shiftId) {
        TypedQuery<Ride> q = em.createQuery("""
                        select r from Ride r
                        join fetch r.matchRequests m
                        join fetch m.shift s
                        join fetch s.driver
                        where r.id = :rideId
                            and s.id = :shiftId
                        """, Ride.class);
        q.setParameter("rideId", rideId);
        q.setParameter("shiftId", shiftId);
        return q.getResultStream().findFirst();
    }

    @Override
    public Ride verifyRideByDriver(Long rideId, Long driverId){
        TypedQuery<Ride> q = em.createQuery("""
                        select r from Ride r
                        join fetch r.shift s
                        join fetch s.driver d
                        where r.id = :rideId
                            and r.rideEndTime is null
                            and d.id = :driverId
                        """, Ride.class);
        q.setParameter("rideId", rideId);
        q.setParameter("driverId", driverId);
        return q.getSingleResult();
    }
    @Override
    public Optional<Ride> verifyRideByCustomer(Long rideId, Long customerId){
        TypedQuery<Ride> q = em.createQuery("""
                        select r from Ride r
                        join r.customer c
                        where r.id = :rideId
                            and r.customer.id = :customerId
                        """, Ride.class);
        q.setParameter("rideId", rideId);
        q.setParameter("customerId", customerId);
        return q.getResultStream().findFirst();
    }

    @Override
    public Optional<Ride> getRideWithRating(Long rideId) {
        TypedQuery<Ride> q = em.createQuery("""
                        select r from Ride r
                        join fetch r.customerRating cr
                        join fetch r.customer
                        where r.id = :rideId
                        """, Ride.class);
        q.setParameter("rideId", rideId);
        return q.getResultStream().findFirst();
    }

    @Override
    public Optional<Ride> getRideForCustomer(Long rideId, Long customerId) {
        TypedQuery<Ride> q = em.createQuery("""
                        select r from Ride r
                        join r.customer c
                        where c.id = :customerId
                            and r.id = :rideId
                        """, Ride.class);
        q.setParameter("rideId", rideId);
        q.setParameter("customerId", customerId);
        return q.getResultStream().findFirst();
    }

    @Override
    public List<Ride> getCustomerRidesFull(Long customerId) {
        TypedQuery<Ride> q = em.createQuery("""
                                            select r from Ride r
                                            join fetch r.customer customer
                                            left join fetch r.shift
                                            left join fetch r.customerRating
                                            left join fetch r.payment
                                            left join fetch r.promocode
                                            left join fetch r.rate
                                            where customer.id = :customerId
                                                and r.rideEndTime is not null
                                            """, Ride.class);
        q.setParameter("customerId", customerId);
        return q.getResultList();
    }

    @Override
    public List<Ride> getDriverRidesFull(Long driverId) {
        TypedQuery<Ride> q = em.createQuery("""
                                            select r from Ride r
                                            join r.shift s
                                            join s.driver d
                                            where d.id = :driverId
                                                and r.rideEndTime is not null
                                            """, Ride.class);
        q.setParameter("driverId", driverId);
        return q.getResultList();
    }

    @Override
    public Optional<Ride> getActiveRideForDriver(Long driverId) {
        TypedQuery<Ride> q = em.createQuery("""
                                            select r from Ride r
                                            join r.shift s
                                            join s.driver d
                                            where d.id = :driverId
                                                and r.rideEndTime is null
                                            """, Ride.class);
        q.setParameter("driverId", driverId);
        return q.getResultStream().findFirst();
    }

    @Override
    public List<Ride> getAllRidesFullWithPaginationAndFiltering(Specification<Ride> specification, PaginationDetails paginationDetails) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        var e = new RuntimeException("custom");
//        System.out.println(e.sta);
        e.printStackTrace();
        var s = e.getStackTrace();
        CriteriaQuery<Ride> cq = cb.createQuery(Ride.class);
        Root<Ride> root = cq.from(Ride.class);

        root.fetch(Ride_.rate);
        root.fetch(Ride_.promocode, JoinType.LEFT);
        root.fetch(Ride_.customerRating, JoinType.LEFT);
        root.fetch(Ride_.payment);
        root.fetch(Ride_.customer);
        root.fetch(Ride_.shift);

        cq.select(root);
        if(specification != null){
            cq.where(specification.toPredicate(root, cq , cb));
        }

        TypedQuery<Ride> query = em.createQuery(cq);
        query.setFirstResult(paginationDetails.getOffset());
        query.setMaxResults(paginationDetails.getLimit());

        return query.getResultList();
    }

    @Override
    public List<Ride> getCustomerRidesFullWithPaginationAndFiltering(Long customerId, Specification<Ride> specification, PaginationDetails paginationDetails) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Ride> cq = cb.createQuery(Ride.class);
        Root<Ride> root = cq.from(Ride.class);

        root.fetch(Ride_.rate);
        root.fetch(Ride_.promocode, JoinType.LEFT);
        root.fetch(Ride_.customerRating, JoinType.LEFT);
        root.fetch(Ride_.payment);


        cq.select(root);

//        spec is never null - at least its conjunction
        cq.where(cb.and(specification != null ? specification.toPredicate(root, cq , cb) : cb.conjunction(),
                    cb.equal(root.get(Ride_.customer).get(User_.userId), customerId)));

        TypedQuery<Ride> query = em.createQuery(cq);
        query.setFirstResult(paginationDetails.getOffset());
        query.setMaxResults(paginationDetails.getLimit());

        return query.getResultList();
    }

    @Override
    public List<Ride> getDriverRidesFullWithPaginationAndFiltering(Long driverId, Specification<Ride> specification, PaginationDetails paginationDetails) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Ride> cq = cb.createQuery(Ride.class);
        Root<Ride> root = cq.from(Ride.class);

        root.fetch(Ride_.rate);
        root.fetch(Ride_.promocode, JoinType.LEFT);
        root.fetch(Ride_.payment);
        root.fetch(Ride_.shift);

        cq.select(root);
        if(specification != null){
            cq.where(specification.toPredicate(root, cq , cb));
        }
        Join<Ride, Shift> shiftJoin = root.join(Ride_.shift);
        cq.where(cb.and(specification != null ? specification.toPredicate(root, cq , cb) : cb.conjunction(),
                cb.equal(shiftJoin.get(Shift_.driver).get(Driver_.driverId), driverId)));

        TypedQuery<Ride> query = em.createQuery(cq);
        query.setFirstResult(paginationDetails.getOffset());
        query.setMaxResults(paginationDetails.getLimit());

        return query.getResultList();
    }

    @Override
    public Optional<Ride> findRideByRequest(Long requestId) {
        TypedQuery<Ride> q = em.createQuery("""
                                        select r from Ride r
                                        join r.matchRequests mr
                                        where mr.id = :requestId
                                        """, Ride.class);
        q.setParameter("requestId", requestId);
        return q.getResultStream().findFirst();
    }

    @Override
    public List<Ride> findRidesForDeactivation() {
        TypedQuery<Ride> q = em.createQuery("""
                                        select r from Ride r
                                        join fetch r.matchRequests mr
                                        where mr.matchRequestStatus = :offered
                                            and FUNCTION('date_add', mr.createdAt, '20 sec') < now()
                                        """, Ride.class);
        q.setParameter("offered", MatchRequestStatus.OFFERED);
        return q.getResultList();
    }


}