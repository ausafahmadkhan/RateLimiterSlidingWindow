package main.java.service;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterServiceImpl implements RateLimiterService
{
    private static RateLimiterService INSTANCE;
    private final ConcurrentHashMap<String, Long> requestCount;
    private final Long WINDOW_SIZE;
    private final Long REQUEST_LIMIT;

    public RateLimiterServiceImpl(Long WINDOW_SIZE, Long REQUEST_LIMIT) {
        this.requestCount = new ConcurrentHashMap<>();
        this.WINDOW_SIZE = WINDOW_SIZE;
        this.REQUEST_LIMIT = REQUEST_LIMIT;
    }

    public static RateLimiterService getInstance(Long windowSize, Long requestLimit)
    {
        if (Objects.isNull(INSTANCE))
            INSTANCE = new RateLimiterServiceImpl(windowSize, requestLimit);

        return INSTANCE;
    }

    @Override
    public boolean isAllowed(String userId)
    {
        Long currentTimeMillis = System.currentTimeMillis();
        String key = userId + currentTimeMillis;

        //cumulative count in the window
        long currentCount;

        currentCount = getCountInTheWindow(userId, currentTimeMillis);
        System.out.println(currentCount);

        if (currentCount >= REQUEST_LIMIT)
            return false;

        requestCount.put(key, requestCount.getOrDefault(key, 0L) + 1);

        return true;
    }

    private long getCountInTheWindow(String userId, Long currentTimeMillis)
    {

        long upperLimit = currentTimeMillis, lowerLimit = upperLimit - WINDOW_SIZE;

        long totalCount = 0;

        for (long i = lowerLimit; i <= upperLimit ; i++)
        {
            if (requestCount.containsKey(userId + i))
                totalCount += requestCount.get(userId + i);
        }

        return totalCount;
    }
}
