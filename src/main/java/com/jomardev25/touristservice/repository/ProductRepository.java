package com.jomardev25.touristservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jomardev25.touristservice.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	Optional<Product> findByName(String name);

}
