package sardine.utils;

import java.util.concurrent.TimeUnit;

/**
 * Created by bruce on 15/11/19.
 */
public class StopWatch {

    private long startTime;

    private StopWatch() {
        reset2Now();
    }

    public static StopWatch newInstance() {
        return new StopWatch();
    }

    public long milliSeconds() {
        return as(TimeUnit.MILLISECONDS);
    }

    public long as(TimeUnit timeUnit) {
        return timeUnit.convert(nanoSeconds(), TimeUnit.NANOSECONDS);
    }

    public long reset() {
        long milliSeconds = milliSeconds();
        reset2Now();
        return milliSeconds;
    }

    public void reset2Now() {
        startTime = System.nanoTime();
    }

    public long nanoSeconds() {
        return System.nanoTime() - startTime;
    }

    @Override
    public String toString() {
        return nanoSeconds() + "nanoSeconds";
    }
}
