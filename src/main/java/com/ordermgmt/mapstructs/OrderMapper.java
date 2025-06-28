package com.ordermgmt.mapstructs;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ordermgmt.models.dto.OrderDTO;
import com.ordermgmt.models.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

	OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

	OrderDTO toDto(Order order);

	Order toEntity(OrderDTO orderDto);

}
