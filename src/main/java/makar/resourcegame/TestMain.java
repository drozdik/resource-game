package makar.resourcegame;

import java.util.concurrent.TimeUnit;

public class TestMain {

    Object mutex;

    public static void main(String[] args) {
        TestMain m = new TestMain();
        Thread a = new Thread(() -> {m.method("key_a");},"thread a");
        Thread b = new Thread(() -> {m.method("key_b");},"thread b");
        a.start();
        b.start();
    }

    public synchronized void method(String monitor){
        synchronized (this){
            System.out.println("Thread " + Thread.currentThread().getName() + " entered method");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(e);
            }
            System.out.println("Thread " + Thread.currentThread().getName() + " exiting method");
        }
    }


}
