package com.threadsafedatastructs.EvictingQueue;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QueueTests {

    @Test
    public void testEmptyObject() {
        EvictingQueue<Integer> a = new EvictingQueue(100);
        assertTrue(a.isEmpty());
        assertTrue(a.size() == 0);
    }

    @Test(expected = NoSuchElementException.class)
    public void testPopFirstEmpty() {
        EvictingQueue<Integer> a = new EvictingQueue(100);
        Object o = a.popFirst();
    }

    @Test(expected = NoSuchElementException.class)
    public void testPopLastEmpty() {
        EvictingQueue<Integer> a = new EvictingQueue(100);
        Object o = a.popLast();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testHeadOfEmpty() {
        EvictingQueue<Integer> a = new EvictingQueue(100);
        Object o = a.head();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTailOfEmpty() {
        EvictingQueue<Integer> a = new EvictingQueue(100);
        Object o = a.tail();
    }

    @Test
    public void testPushLessThanMax() {
        EvictingQueue<Integer> a = new EvictingQueue(100);
        a.push(1);
        a.push(2);
        assertTrue(!a.isEmpty());
        assertTrue(a.size() == 2);
        assertTrue(a.toString().equals("[2, 1]"));
    }

    @Test
    public void testAppendLessThanMax() {
        EvictingQueue<Integer> a = new EvictingQueue(100);
        a.append(1);
        a.append(2);
        assertTrue(!a.isEmpty());
        assertTrue(a.size() == 2);
        assertTrue(a.toString().equals("[1, 2]"));
    }

    @Test
    public void testPopFirstNonEmpty() {
        EvictingQueue<Integer> a = new EvictingQueue(100);
        a.push(1);
        a.push(2);
        a.popFirst();
        a.popFirst();
        assertTrue(a.size() == 0);
        assertTrue(a.isEmpty());
    }

    @Test
    public void testPopLastNonEmpty() {
        EvictingQueue<Integer> a = new EvictingQueue(100);
        a.push(1);
        a.push(2);
        a.popLast();
        a.popFirst();
        assertTrue(a.size() == 0);
        assertTrue(a.isEmpty());
    }

    @Test
    public void testPushIfMax() {
        EvictingQueue<Integer> a = new EvictingQueue(100);
        for (int i = 0; i < a.getMAX_SIZE(); i++) {
            a.push(1);
        }
        a.append(2);
        assertTrue(a.size() == 100);
        assertTrue(a.head() == 2);
    }

    @Test
    public void testAppendIfMax() {
        EvictingQueue<Integer> a = new EvictingQueue(100);
        for (int i = 0; i < a.getMAX_SIZE(); i++) {
            a.append(1);
        }
        a.push(2);
        assertTrue(a.size() == 100);
        assertTrue(a.head() == 2);
    }

    @Test
    public void testPollAllFromEmpty() {
        EvictingQueue<Integer> a = new EvictingQueue(100);
        Object o = a.pollAll();
        assertEquals(o.toString(), "[]");
    }

    @Test
    public void testPollAll() {
        EvictingQueue<Integer> a = new EvictingQueue(100);
        a.push(1);
        a.append(2);
        a.push(3);
        Object o = a.pollAll();
        assertEquals(o.toString(), "[3, 1, 2]");
        assertEquals(a.toString(), "[]");

    }

    @Test
    public void testSynchronizedPushFull() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        EvictingQueue<Integer> a = new EvictingQueue(100);

        for (int i = 0; i < 101; i++) {
            int finalI = i;
            executorService.submit(() -> a.push(finalI));
        }

        executorService.shutdown();
        executorService.awaitTermination(40, TimeUnit.SECONDS);

        assertEquals(a.head().toString(), "100");
        assertEquals(a.size(), 100);
    }

    @Test
    public void testSynchronizedAppendFull() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        EvictingQueue<Integer> a = new EvictingQueue(100);

        for (int i = 0; i < 101; i++) {
            int finalI = i;
            executorService.submit(() -> a.append(finalI));
        }

        executorService.shutdown();
        executorService.awaitTermination(40, TimeUnit.SECONDS);

        assertEquals(a.head().toString(), "100");
        assertEquals(a.size(), 100);
    }


    @Test
    public void testSynchronizedPush() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        EvictingQueue<Integer> a = new EvictingQueue<>(100);

        for (int i = 0; i < 50; i++) {
            int finalI = i;
            executorService.submit(() -> a.push(finalI));
        }
        executorService.shutdown();
        executorService.awaitTermination(40, TimeUnit.SECONDS);

        assertEquals(a.head().toString(), "49");
        assertEquals(a.tail().toString(), "0");
        assertEquals(a.size(), 50);
    }

    @Test
    public void testSynchronizedAppend() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        EvictingQueue<Integer> a = new EvictingQueue<>(100);
        for (int i = 0; i < 50; i++) {
            int finalI = i;
            executorService.submit(() -> a.append(finalI));
        }
        executorService.shutdown();
        executorService.awaitTermination(40, TimeUnit.SECONDS);

        assertEquals(a.head().toString(), "0");
        assertEquals(a.tail().toString(), "49");
        assertEquals(a.size(), 50);
    }

    @Test
    public void testSynchronizedPopFirst() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        EvictingQueue<Integer> a = new EvictingQueue<>(100);
        for (int k = 0; k < 10; k++)
            a.push(k);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> a.popFirst());
        }
        executorService.shutdown();
        executorService.awaitTermination(40, TimeUnit.SECONDS);
        assertTrue(a.isEmpty());
    }

    @Test
    public void testSynchronizedPopLastSize() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        EvictingQueue<Integer> a = new EvictingQueue<>(100);
        for (int k = 0; k < 10; k++)
            a.push(k);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> a.popLast());
        }
        executorService.shutdown();
        executorService.awaitTermination(40, TimeUnit.SECONDS);
        assertTrue(a.isEmpty() && a.size() == 0);
    }

    @Test
    public void testSynchronizedPollAll() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        EvictingQueue<Integer> a = new EvictingQueue(100);
        ArrayDeque<Integer> o;
        ArrayDeque<Integer> tmp = new ArrayDeque<>(new ArrayList<>(Arrays.asList(0, 1, 2, 3)));
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            executorService.submit(() -> a.append(finalI));
        }
        o = a.pollAll();
        executorService.shutdown();
        executorService.awaitTermination(40, TimeUnit.SECONDS);
        assertEquals(o.toString(), tmp.toString());
    }

    @Test
    public void testSynchronizedIsEmpty() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        EvictingQueue<Integer> a = new EvictingQueue(100);
        for (int k = 0; k < 20; k++)
            a.push(k);

        for (int i = 0; i < 20; i++) {
            executorService.submit(() -> a.popFirst());
        }
        executorService.shutdown();
        executorService.awaitTermination(40, TimeUnit.SECONDS);
        assertEquals(a.isEmpty(), true);
    }
}
