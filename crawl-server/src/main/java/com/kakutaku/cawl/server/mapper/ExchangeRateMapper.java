package com.kakutaku.cawl.server.mapper;

import com.kakutaku.cawl.server.entity.ExchangeRate;

public interface ExchangeRateMapper {

    int insert(ExchangeRate record);

    int insertSelective(ExchangeRate record);
}