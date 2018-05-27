package com.storemanagement.dao.order;

import com.storemanagement.entities.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface StatusOwnRepository extends Repository<Status, Long> {
    @Query(value="from Status s where s.id=?1")
    Status getStatusById(long id);

    @Query(value="from Status s where s.name=?1")
    Status getStatusByName(String name);
}
