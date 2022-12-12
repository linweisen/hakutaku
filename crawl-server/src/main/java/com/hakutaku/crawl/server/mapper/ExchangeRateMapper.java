package com.hakutaku.crawl.server.mapper;

import com.hakutaku.crawl.server.entity.ExchangeRate;

public interface ExchangeRateMapper {

    int insert(ExchangeRate record);

    int insertSelective(ExchangeRate record);
}