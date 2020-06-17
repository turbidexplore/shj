package com.turbid.explore.service;

import com.turbid.explore.pojo.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    Order save(Order order);

    Order findByOrderNo(String orderno);

    List<Order> findByUser(String name, Integer page);

    List<Order> findByUserIos(String name, Integer page);
}
