package com.javakaian.shooter.utils;

import com.javakaian.shooter.utils.stats.GameStats;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameStatsConcurrencyTest {

    @Test
    void concurrentGetInstanceReturnsOnlyOneSingleton() throws Exception {
        int threadCount = 50;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startGate = new CountDownLatch(1);

        List<Callable<GameStats>> tasks = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            tasks.add(() -> {
                startGate.await();
                return GameStats.getInstance();
            });
        }

        List<Future<GameStats>> futures = new ArrayList<>();
        for (Callable<GameStats> task : tasks) {
            futures.add(pool.submit(task));
        }

        startGate.countDown();

        Set<GameStats> instances = new HashSet<>();
        for (Future<GameStats> future : futures) {
            instances.add(future.get(10, TimeUnit.SECONDS));
        }

        assertEquals(1, instances.size(),
                "All threads must receive the same singleton instance");

        GameStats verifyInstance = GameStats.getInstance();
        assertEquals(instances.iterator().next(), verifyInstance,
                "Subsequent getInstance() calls must return the same instance");

        pool.shutdownNow();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    void concurrentUpdatesAccumulateWithoutLostWrites() throws Exception {
        GameStats stats = GameStats.getInstance();

        stats.resetTotals();
        stats.resetSession();

        stats.startSession();

        int shotsThreads = 8;
        int shotsPerThread = 1000;
        int expectedSessionShots = shotsThreads * shotsPerThread;

        int damageThreads = 4;
        int damagePerThreadIters = 500;
        float damageAmount = 0.2f;
        double expectedSessionDamage = damageThreads * damagePerThreadIters * damageAmount;

        int distanceThreads = 4;
        int distancePerThreadIters = 500;
        float distanceAmount = 1.5f;
        double expectedSessionDistance = distanceThreads * distancePerThreadIters * distanceAmount;

        int deathThreads = 6;
        int deathsPerThread = 300;
        int expectedTotalDeaths = deathThreads * deathsPerThread;

        int poolSize = 16;
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        CountDownLatch startGate = new CountDownLatch(1);

        List<Callable<Void>> tasks = new ArrayList<>();

        for (int t = 0; t < shotsThreads; t++) {
            tasks.add(() -> {
                startGate.await();
                for (int i = 0; i < shotsPerThread; i++) {
                    stats.incrementShotsFired();
                }
                return null;
            });
        }

        for (int t = 0; t < damageThreads; t++) {
            tasks.add(() -> {
                startGate.await();
                for (int i = 0; i < damagePerThreadIters; i++) {
                    stats.addDamageTaken(damageAmount);
                }
                return null;
            });
        }

        for (int t = 0; t < distanceThreads; t++) {
            tasks.add(() -> {
                startGate.await();
                for (int i = 0; i < distancePerThreadIters; i++) {
                    stats.addDistanceTraveled(distanceAmount);
                }
                return null;
            });
        }

        for (int t = 0; t < deathThreads; t++) {
            tasks.add(() -> {
                startGate.await();
                for (int i = 0; i < deathsPerThread; i++) {
                    stats.incrementDeaths();
                }
                return null;
            });
        }

        List<Future<Void>> futures = new ArrayList<>(tasks.size());
        for (Callable<Void> task : tasks) {
            futures.add(pool.submit(task));
        }

        startGate.countDown();

        for (Future<Void> f : futures) {
            f.get(10, TimeUnit.SECONDS);
        }

        stats.endSession();

        assertEquals(1, stats.getTotalSessions(), "totalSessions should be 1 after startSession");
        assertEquals(expectedTotalDeaths, stats.getTotalDeaths(), "All death increments should be observed");
        assertEquals(expectedSessionShots, stats.getTotalShots(), "All shot increments should be observed after endSession");

        assertEquals(expectedSessionDamage, stats.getTotalDamage(), 1e-2, "All damage increments should be accumulated");
        assertEquals(expectedSessionDistance, stats.getTotalDistance(), 1e-2, "All distance increments should be accumulated");

        pool.shutdownNow();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }
}
