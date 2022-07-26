package main.java;

import main.java.service.RateLimiterService;
import main.java.service.RateLimiterServiceImpl;

public class Application
{
    public static void main(String[] args) {
        RateLimiterService rateLimiterService = RateLimiterServiceImpl.getInstance(1000L, 5L);

        for (int i = 0; i < 10 ; i++)
        {
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            boolean isAllowed = rateLimiterService.isAllowed(1 + "");
            System.out.println("Req allowed : " + isAllowed);
        }
    }
}
