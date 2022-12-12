package com.kakutaku.cawl.server.cron;

import com.kakutaku.cawl.server.entity.ExchangeRate;
import com.kakutaku.cawl.server.mapper.ExchangeRateMapper;
import com.kakutaku.cawl.server.utils.HttpUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostConstruct
    @Scheduled(cron = "0 0 */1 * * ?")
    public void task() {
        logger.info("启动汇率线程");
        String r = HttpUtils.get("https://www.mxnzp.com/api/exchange_rate/aim?from=USD&to=CNY&app_id=tihneotkisusvmev&app_secret=OGpaU3BrWTZTUjJpNGZDa1VJdnVZdz09");
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
