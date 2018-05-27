package com.storemanagement.services.order;

import com.storemanagement.entities.Status;

public interface StatusService {
    Status getStatusByName(String name);
}
