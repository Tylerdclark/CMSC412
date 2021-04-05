#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>

int main()
{
    pid_t pid, g_pid;
    g_pid = getpid();
    pid = fork();

    switch (pid) {
        case -1:
            printf("Fork error.");
            break;
        case 0: // the child process gets 0
            pid = fork();
            switch (pid) {
                case -1:
                    printf("Fork error.");
                    break;
                case 0: // is the child of the child of the ancestor
                    printf("I am the child process C and my pid is %d. My parent P has pid %d. My grandparent G has pid %d.\n", getpid(), getppid(), g_pid);
                    break;
                default:
                    wait(NULL);
                    printf("I am the parent process P and my pid is %d. My parent G has pid %d\n", getpid(), getppid());
                    break;
            }
            break;
        default:
            wait(NULL);
            printf("I am the Grandparent process G and my pid is %d\n", getpid());
            break;
    }
}