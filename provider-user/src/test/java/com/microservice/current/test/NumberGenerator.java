package com.microservice.current.test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberGenerator{
    private volatile int currentMinute = LocalDateTime.now().get(ChronoField.MINUTE_OF_DAY);
    private AtomicInteger counter = new AtomicInteger(0);

    public String nextNumber(){
    
        int min = LocalDateTime.now().get(ChronoField.MINUTE_OF_DAY);

        if(currentMinute == min){
            return currentMinute + "" + counter.incrementAndGet();
        }else{
            counter.set(0);
            currentMinute = LocalDateTime.now().getMinute();
            return currentMinute + "" + counter.incrementAndGet();
        }
    }
}
