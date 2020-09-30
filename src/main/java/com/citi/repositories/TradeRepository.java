package com.citi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.citi.entity.Trade;

public interface TradeRepository extends CrudRepository<Trade, Long> {

}
