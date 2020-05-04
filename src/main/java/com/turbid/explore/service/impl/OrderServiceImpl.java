package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Order;
import com.turbid.explore.repository.OrderRepository;

import com.turbid.explore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        return orderRepository.saveAndFlush(order);
    }

    @Override
    public Order findByOrderNo(String orderno) {
        return orderRepository.findByOrderNo(orderno);
    }
}
