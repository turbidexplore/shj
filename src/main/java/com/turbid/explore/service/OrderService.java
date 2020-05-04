package com.turbid.explore.service;

import com.turbid.explore.pojo.Order;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

    Order save(Order order);

    Order findByOrderNo(String orderno);
}
