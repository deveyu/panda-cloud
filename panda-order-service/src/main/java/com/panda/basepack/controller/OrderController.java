package com.panda.basepack.controller;


import com.baomidou.mybatisplus.annotation.TableName;
import com.panda.basepack.entity.dto.OrderDTO;
import com.panda.basepack.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OrderController {

    private OrderService orderService;

    @PostMapping("insert")
    public ResponseEntity<Long> insertOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return null;
    }
}
