package com.storemanagement.servicesImpl.order;

import com.storemanagement.dao.order.StatusOwnRepository;
import com.storemanagement.dao.order.StatusRepository;
import com.storemanagement.entities.Status;
import com.storemanagement.services.order.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class StatusServiceImpl implements StatusService {
    @Autowired
    private StatusOwnRepository statusOwnRepository;
    @Autowired
    private StatusRepository statusRepository;


    @Override
    public Status getStatusByName(String name) {
        return statusOwnRepository.getStatusByName(name);
    }
}
