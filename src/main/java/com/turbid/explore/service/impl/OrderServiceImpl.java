package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Follow;
import com.turbid.explore.pojo.Order;
import com.turbid.explore.repository.OrderRepository;

import com.turbid.explore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Order> findByUser(String name, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Order> pages=  orderRepository.findByUser(pageable,name);
        return pages.getContent();
    }
}
