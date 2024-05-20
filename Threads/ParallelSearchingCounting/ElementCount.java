package aud6_ThreadExercises.ParallelSearchingCounting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ElementCount {
    static long count = 0;
    static Lock lock = new ReentrantLock();
    static long MAX = 0;

    static Semaphore doneSemaphore = new Semaphore(0);
    static Semaphore canCheckMax = new Semaphore(0);

    static BoundedRandomGenerator random = new BoundedRandomGenerator();

    private static final int ARRAY_LENGTH = 1000;
    private static final int NUM_THREADS = 10;

    private static final int SEARCH_TARGET = random.nextInt(); // Generate SEARCH_TARGET after BoundedRandomGenerator instantiation

    public static int[] getSubArray(int[] array, int start, int end) {
        return Arrays.copyOfRange(array, start, end);
    }

    public static void main(String[] args) throws InterruptedException {
        int[] arr = ArrayGenerator.generate(ARRAY_LENGTH, SEARCH_TARGET);

        System.out.printf("Search target is: %d \n", SEARCH_TARGET);
        System.out.println("Generated elements are: ");
        for (int i = 0; i < ArrayGenerator.elementCount; i++) {
            System.out.println(arr[i]);
        }

        //TODO make the searchThread class a thread and start 10 instances
        //Each instance should take a subarray from the original array with equal length

        int elementsPerThread = arr.length / NUM_THREADS;
        List<CountThread> countThreads = new ArrayList<>();
        for (int i = 0; i < NUM_THREADS; i++) {
            int[] subArray = getSubArray(arr, i * elementsPerThread, (i + 1) * elementsPerThread);
            countThreads.add(new CountThread(subArray, SEARCH_TARGET));
        }

        for (CountThread countThread : countThreads) {
            countThread.start();
        }

        //ALL done, check max count
        doneSemaphore.acquire(NUM_THREADS);
        canCheckMax.release(NUM_THREADS);
        //TODO Start the 10 threads

        System.out.println("Total counted elements: " + count);
        System.out.println("Generated number of elements is: " + ArrayGenerator.elementCount);

        //TODO the max thread should print the number of occurrences

    }

    static class CountThread extends Thread {
        private int[] arr;
        private int target;

        public CountThread(int[] arr, int target) {
            this.arr = arr;
            this.target = target;
        }

        public void countElements() {
            for (int num : this.arr) {
                if (num == target) {
                    count++;
                }
            }
        }

        public void countElementsParallel() throws InterruptedException {
            //TODO implement and run the parallel counting method from the thread
            int localCount = 0;

            for (int num : this.arr) {
                if (num == target) {
                    localCount++;
                }
            }
            lock.lock();
            count += localCount;

            if (localCount > MAX) {
                MAX = localCount;
            }
            lock.unlock();

            //im done
            doneSemaphore.release();
            canCheckMax.acquire();

            if (localCount == MAX) {
                System.out.printf("Thread with ID %d counted max %d \n", Thread.currentThread().getId(), localCount);
            }
        }


        public void run() {
            try {
                countElementsParallel();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class BoundedRandomGenerator {
        static final java.util.Random random = new java.util.Random();
        static final int RANDOM_BOUND = 100;

        public int nextInt() {
            return random.nextInt(RANDOM_BOUND);
        }
    }

    static class ArrayGenerator {
        static int elementCount;

        static int[] generate(int length, int target) {
            int[] array = new int[length];

            for (int i = 0; i < length; i++) {
                int element = ElementCount.random.nextInt();

                if (element == target) {
                    elementCount++;
                }
                array[i] = element;
            }
            return array;
        }
    }
}
