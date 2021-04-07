#include <cstdio>
#include <cstdlib>
#include <pthread.h>

const int THREAD_COUNT{3};
const int ITERATIONS{5};
int current_iteration{0};

pthread_mutex_t mutex;
pthread_cond_t cond;

struct ThreadData {
    int current;
    int next;
};

void *routine(void *arg) {
    auto *data = (ThreadData *) arg; //type inference for extra safety
    pthread_mutex_lock(&mutex);

    for (int i = 0; i < ITERATIONS; i++) {
        while (current_iteration != data->current) { //find the right thread
            pthread_cond_wait(&cond, &mutex);
        }
        printf("Thread %d - iteration no. %d\n", data->current + 1, i + 1); // +1 for zero indexing
        current_iteration = data->next;
        pthread_cond_broadcast(&cond); // unblock all threads
    }
    pthread_mutex_unlock(&mutex);
    pthread_exit(nullptr);
}

int main() {
    ThreadData data[THREAD_COUNT];
    pthread_t threads[THREAD_COUNT];

    pthread_mutex_init(&mutex, nullptr);
    pthread_cond_init(&cond, nullptr);

    for (int i = 0; i < THREAD_COUNT; i++) {
        data[i].current = i;
        data[i].next = (i == THREAD_COUNT - 1) ? 0 : i + 1; // last one points to the first

        if (pthread_create(&threads[i], nullptr, routine, (void *) &data[i]) != 0) {
            printf("Issue with creating thread %d\n", i + 1);
            exit(-1);
        } //could add similar if statements to the other mutex initializations,
          // locks, unlocks
    }

    for (int i = 0; i <= THREAD_COUNT - 1; i++)
        if (pthread_join(threads[i], nullptr) != 0) { //wait for all the threads
            printf("Issue with joining thread %d\n", i + 1);
            exit(-1);
        }
    return 0;
}