package com.jomardev25.touristservice.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "orders")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String orderedBy;

	private Date dateOrdered;

	@OneToOne
	@JoinColumn(name = "product", referencedColumnName = "name")
	private Product product;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	private OrderStatus status = OrderStatus.PENDING;

}
