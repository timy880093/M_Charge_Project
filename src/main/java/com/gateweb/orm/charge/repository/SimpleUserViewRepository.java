package com.gateweb.orm.charge.repository;


import com.gateweb.orm.charge.entity.view.SimpleUserView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimpleUserViewRepository extends JpaRepository<SimpleUserView, Integer> {
}
