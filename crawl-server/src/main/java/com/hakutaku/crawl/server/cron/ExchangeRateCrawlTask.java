package com.hakutaku.crawl.server.cron;

import com.hakutaku.crawl.server.entity.ExchangeRate;
import com.hakutaku.crawl.server.mapper.ExchangeRateMapper;
import com.hakutaku.crawl.server.utils.HttpUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Configuration
@EnableScheduling
public class ExchangeRateCrawlTask {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateCrawlTask.class);

    @Autowired
    private ExchangeRateMapper exchangeRateMapper;

    @Value("et.app_id")
    private String appId;

    @Value("et.app_secret")
    private String appSecret;

    @PostConstruct
    @Scheduled(cron = "0 0 */1 * * ?")
    public void task() {
        logger.info("启动汇率线程");
        String r = HttpUtils.get(String.format("https://www.mxnzp.com/api/exchange_rate/aim?from=USD&to=CNY&app_id=%s&app_secret=%s", appId, appSecret));
        JSONObject jsonObject = JSONObject.fromObject(r);
        if (jsonObject.getInt("code") == 1) {
            JSONObject data = jsonObject.getJSONObject("data");
            ExchangeRate rate = new ExchangeRate();
            rate.setPrice(new BigDecimal(data.getDouble("price")));
            rate.setDescription(data.getString("nameDesc"));
            rate.setFromCountry(data.getString("from"));
            rate.setToCountry(data.getString("to"));
            exchangeRateMapper.insert(rate);
        }

    }
}
