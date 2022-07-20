package main.java.service;

public interface RateLimiterService
{
    boolean isAllowed(String userId);
}
