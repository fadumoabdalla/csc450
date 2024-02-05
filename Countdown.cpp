#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>

std::mutex mtx;  
std::condition_variable cv;  
void CountUp() {
    for (int i = 1; i <= 20; ++i) {
        std::unique_lock<std::mutex> lock(mtx);
        while (!ready) {
            cv.wait(lock);
        }
        std::cout << "Thread 1: " << i << std::endl;
        ready = false;
        cv.notify_all();
    }
}

void CountDown() {
    for (int i = 20; i >= 0; --i) {
        std::unique_lock<std::mutex> lock(mtx);
        while (ready) {
            cv.wait(lock);
        }
        std::cout << "Thread 2: " << i << std::endl;
        ready = true;
        cv.notify_all();
    }
}

int main() {
    std::thread t1(CountUp);
    std::thread t2(CountDown);

    t1.join();
    t2.join();

    return 0;
}
