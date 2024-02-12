public class CounterApplication {
    private static final Object lock = new Object();
    private static boolean reachedTwenty = false;

    static class CountUpThread extends Thread {
        public void run() {
            for (int i = 1; i <= 20; i++) {
                synchronized (lock) {
                    System.out.println("Count Up: " + i);
                    if (i == 20) {
                        reachedTwenty = true;
                        lock.notifyAll();
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    static class CountDownThread extends Thread {
        public void run() {
            synchronized (lock) {
                while (!reachedTwenty) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            for (int i = 20; i >= 0; i--) {
                System.out.println("Count Down: " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread countUpThread = new CountUpThread();
        Thread countDownThread = new CountDownThread();

        countUpThread.start();
        countDownThread.start();
    }
}
