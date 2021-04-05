#include <stdio.h>
#include <pthread.h>
#include <unistd.h>

void* routine()
{
    printf("I'm in a thread! PID:%d\n", getpid());
}


int main()
{
    pthread_t thread1, thread2;
    pthread_create(&thread1, NULL, &routine, NULL); //create 1st thread
    pthread_create(&thread2, NULL, &routine, NULL); //create 2nd thread

    pthread_join(thread1, NULL); //blocks calling thread until thread1 terminates
    pthread_join(thread2, NULL); //blocks calling thread until thread2 terminates
    return 0;
}