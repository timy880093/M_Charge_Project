package com.gateweb.einv.vo;

import com.gateweb.einv.model.OrderDetailsEntity;
import com.gateweb.einv.model.OrderMainEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eason on 3/7/2018.
 */
public class OrderVO implements Serializable {
    OrderMainEntity orderMainEntity;
    List<OrderDetailsEntity> orderDetailsEntityList = new ArrayList<>();

    public List<OrderDetailsEntity> getOrderDetailsEntityList() {
        return orderDetailsEntityList;
    }

    public void setOrderDetailsEntityList(List<OrderDetailsEntity> orderDetailsEntityList) {
        this.orderDetailsEntityList = orderDetailsEntityList;
    }

    public OrderMainEntity getOrderMainEntity() {
        return orderMainEntity;
    }

    public void setOrderMainEntity(OrderMainEntity orderMainEntity) {
        this.orderMainEntity = orderMainEntity;
    }
}
