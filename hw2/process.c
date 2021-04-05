#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

int main()
{
    int pid = fork(); // create another process at this point
    printf("I'm in a process! PID:%d\n", getpid());

    if (pid == 0){
        int pid2 = fork();
        printf("I'm in a process! PID:%d\n", getpid());
    }
    if(pid != 0){
        wait(NULL); //wait for child process to finish execution
    }
    return 0;
}