package com.order.in.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.in.common.Payment;
import com.order.in.entity.Order;
import com.order.in.service.OrderService;
import com.order.in.service.PaymentClient;
import com.order.in.vo.TransactionRequest;
import com.order.in.vo.TransactionResponse;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    private OrderService orderService;

    @Autowired
    private PaymentClient paymentClient;
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "/saveOrder")
    public ResponseEntity<Order> placeOrder(@RequestBody Order order){

        return new ResponseEntity<Order>(orderService.placeOrder(order), HttpStatus.CREATED);

    }

    @GetMapping(value = "/allOrders")
    public ResponseEntity<List<Order>> getAllOrders(){

        return new ResponseEntity<List<Order>>(orderService.getAllOrders(), HttpStatus.OK);

    }

    @GetMapping(value = "/{orderId}")
    public ResponseEntity<Order> getSingleOrder(@PathVariable Long orderId){

        return new ResponseEntity<Order>(orderService.findOrder(orderId), HttpStatus.OK);

    }
    
    
    @PostMapping(value = "/placeOrder")
    public ResponseEntity<TransactionResponse> placeOrder(@RequestBody TransactionRequest transactionRequest){

       Order order= transactionRequest.getOrder();
       Order savedOrder=orderService.placeOrder(order);
       Payment payment=transactionRequest.getPayment();

       payment.setAmount(order.getAmount());
       payment.setOrderId(savedOrder.getOrderId());

       // REST API CALL TO PAYMENT SERVICE

       Payment savedPayment=paymentClient.makePayment(payment);

       TransactionResponse transactionResponse=new TransactionResponse();
       transactionResponse.setOrderId(savedOrder.getOrderId());
       transactionResponse.setOrderName(savedOrder.getOrderName());
       transactionResponse.setAmount(savedOrder.getAmount());
       transactionResponse.setTransactionId(savedPayment.getTransactionId());
       transactionResponse.setPaymentStatus(savedPayment.getPaymentStatus());

       return new ResponseEntity<TransactionResponse>(transactionResponse,HttpStatus.CREATED);
    }
    
}
