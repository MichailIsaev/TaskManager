package com.repositories;

import com.taskmanagerlogic.Action;
import com.taskmanagerlogic.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface JournalRepository extends MongoRepository<Task, UUID> {

    void deleteAllByEmail(String email);

    void deleteByName(String name);

    void deleteByStatus(Action status);

    List<Task> findByName(String name);

    List<Task> findByStatus(Pageable pageable, Action status);

    List<Task> findByStatusAndEmail(Pageable pageable, Action status, String email);

    List<Task> findByStatus(Action status);

    List<Task> findByNameStartingWith(String part);

    List<Task> findByTargetTimeBetween(Date from, Date to);

    void deleteByTargetTimeBetween(Date from, Date to);

    List<Task> findByTargetTimeBetween(Pageable pageable, Date from, Date to);

    List<Task> findByTargetTimeBetweenAndEmail(Pageable pageable, Date from, Date to, String email);

    List<Task> findByNameStartingWith(Pageable pageable, String part);

    List<Task> findAllByEmail(Pageable pageable, String email);

    List<Task> findByNameStartingWithAndEmail(Pageable pageable, String part, String email);

    long countByTargetTimeBetweenAndEmail(Date from, Date to, String email);

    long countByTargetTimeBetweenAndNameStartingWith(Date from, Date to, String name);

    long countByTargetTimeBetweenAndNameStartingWithAndEmail(Date from, Date to, String email , String name);

    long countByTargetTimeBetween(Date from, Date to);

    long countByEmail(String email);

    long countByNameStartingWith(String part);

    long countByNameStartingWithAndEmail(String part , String email);

    List<Task> findByTargetTimeBetweenAndNameStartingWith(Pageable pageable, Date from, Date to, String name);

    List<Task> findByTargetTimeBetweenAndNameStartingWithAndEmail(Pageable pageable, Date from, Date to, String name , String email);


}
