package com.repositories;

import com.taskmanagerlogic.ErrorRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Repository("error_record")
public interface ErrorRepository extends MongoRepository<ErrorRecord, UUID> {

}
