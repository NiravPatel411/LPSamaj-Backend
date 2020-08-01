package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.Observation;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Observer;

@Repository
public interface ObservationRepository extends JpaRepository<Observation,String> {

    List<Observation> findAllByObserver(User Observer);
    List<Observation> findAllByobserving(User Observing);

    int countAllByObserver(User Observer);
    int countAllByObserving(User observing);
}
