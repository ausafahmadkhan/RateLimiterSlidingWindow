package main.java.service;

import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterServiceImpl implements RateLimiterService
{

    private static final ConcurrentHashMap<String, Long> requestCount;
    private static final Long WINDOW_SIZE;
    private static final Long REQUEST_LIMIT;


    static
    {
        requestCount = new ConcurrentHashMap<>();
        WINDOW_SIZE = 10L;
        REQUEST_LIMIT = 5L;
    }

    @Override
    public boolean isAllowed(String userId)
    {
        Long time = System.currentTimeMillis();
        String key = userId + time;

        //cumulative count in the window
        long currentCount;

        currentCount = getCountInTheWindow(userId, time);
        System.out.println(currentCount);

        if (currentCount >= REQUEST_LIMIT)
            return false;

        if (requestCount.containsKey(key))
            requestCount.put(key, requestCount.get(key) + 1);
        else
            requestCount.put(key, 1L);

        return true;
    }

    private long getCountInTheWindow(String userId, Long time)
    {

        long upperLimit = time, lowerLimit = upperLimit - WINDOW_SIZE;

        long totalCount = 0;

        for (long i = lowerLimit; i <= upperLimit ; i++)
        {
            if (requestCount.containsKey(userId + i))
                totalCount += requestCount.get(userId + i);
        }

        return totalCount;
    }
}
