package com.example.aflfa.service.Imp;

import com.example.aflfa.feign.FeignOpenExchangeRates;
import com.example.aflfa.model.ExchangeRates;
import com.example.aflfa.service.inter.OpenExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class OpenExchangeImp implements OpenExchangeService {


    private ExchangeRates prevRates;
    private ExchangeRates currentRates;

    @Autowired
    private FeignOpenExchangeRates feignOpenExchangeRates;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    @Value("85cf03f223cd4a39aa04a024df872f79")
    private String appId;
    @Value("${openexchangerates.base}")
    private String base;

    @Autowired
    public OpenExchangeImp(FeignOpenExchangeRates feignOpenExchangeRates, @Qualifier("date_bean") SimpleDateFormat dateFormat, @Qualifier("time_bean") SimpleDateFormat timeFormat)
    {
        this.feignOpenExchangeRates = feignOpenExchangeRates;
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
    }

    @Override
    public List<String> getCharCodes() {
        List<String> result = null;
        if (this.feignOpenExchangeRates.getLatestRates(appId).getRates() != null) {
            result = new ArrayList<>(this.feignOpenExchangeRates.getLatestRates(appId).getRates().keySet());
        }
        return result;
    }

    private Double getCoefficient(ExchangeRates rates, String charCode) {
        Double result = null;
        Double targetRate = null;
        Double appBaseRate = null;
        Double defaultBaseRate = null;
        Map<String, Double> map = null;
        if (rates != null && rates.getRates() != null) {
            map = rates.getRates();
            targetRate = map.get(charCode);
            appBaseRate = map.get(this.base);
            defaultBaseRate = map.get(rates.getBase());
        }
        if (targetRate != null && appBaseRate != null && defaultBaseRate != null) {
            result = new BigDecimal(
                    (defaultBaseRate / appBaseRate) * targetRate
            )
                    .setScale(4, RoundingMode.UP)
                    .doubleValue();
        }
        return result;
    }

    private void refreshPrevRates(long time) {
        Calendar prevCalendar = Calendar.getInstance();
        prevCalendar.setTimeInMillis(time);
        String currentDate = dateFormat.format(prevCalendar.getTime());
        prevCalendar.add(Calendar.DAY_OF_YEAR, -1);
        String newPrevDate = dateFormat.format(prevCalendar.getTime());
        if (
                this.prevRates == null
                        || (
                        !dateFormat.format(Long.valueOf(this.prevRates.getTimestamp()) * 1000)
                                .equals(newPrevDate)
                                && !dateFormat.format(Long.valueOf(this.prevRates.getTimestamp()) * 1000)
                                .equals(currentDate)
                )
        ) {
            this.prevRates = feignOpenExchangeRates.getHistoricalRates(newPrevDate, appId);
        }
    }


    private void refreshCurrentRates(long time) {
        if (
                this.currentRates == null ||
                        !timeFormat.format(Long.valueOf(this.currentRates.getTimestamp()) * 1000)
                                .equals(timeFormat.format(time))
        ) {
            this.currentRates = feignOpenExchangeRates.getLatestRates(this.appId);
        }
    }

    @Override
    public void refreshRates() {
        long currentTime = System.currentTimeMillis();
        this.refreshCurrentRates(currentTime);
        this.refreshPrevRates(currentTime);
    }



    @Override
    public int getKeyForTag(String charCode) {
        this.refreshRates();
        Double prevCoefficient = this.getCoefficient(this.prevRates, charCode);
        Double currentCoefficient = this.getCoefficient(this.currentRates, charCode);
        return prevCoefficient != null && currentCoefficient != null
                ? Double.compare(currentCoefficient, prevCoefficient)
                : -101;
    }


}
