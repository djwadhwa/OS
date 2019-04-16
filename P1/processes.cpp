#include <iostream>
#include  <stdio.h>
// #include  <string.h>
#include <unistd.h>
#include <sys/wait.h>

// DJ Wadhwa
// 04/14/2019

int main (int argc, char **argv)
{
  pid_t pid;
  int status;
  if (argc != 2)
  {
    perror ("Too many or too few arguements."); //print error if not enough argument
    return 0;
  }
  enum {RD, WR};
  int pipe1[2]; //pipe one between child and grandchild
  int pipe2[2]; //pipe two between grandchild and great-grandchild
/**
 * FD TABLE
 * 0 stdin
 * 1 stdout
 * 2 stderr
 * 3 pipe1 in
 * 4 pipe1 out
 * 5 pipe2 in
 * 6 pipe2 out
 */

  if ((pid = fork()) < 0) //fork and create child
  {
     perror("Child process could not be created."); //print error if fork failed
  }
  else if (pid == 0)
  {
    if (pipe (pipe1)<0){
      //create first pipe
      perror("Error creating pipe between child and grandchild.");
    }
    if (pipe (pipe2)<0){
      //create second pipe
      perror("Error creating pipe between grandchild and great-grandchild.");
    }
    if ((pid = fork())< 0) //fork and create grandchild
    {
      perror("Grandchild process could not be created."); //fork failure error
    }
    else if (pid == 0)
    {
      if ((pid = fork())< 0) //fork and create great-grandchild
      {
        perror("Great-grandchild process could not be created.");
      }
      else if (pid == 0)
      {
        //great Grandchild
        close (pipe1[RD]); //close pipe 2 read
        close (pipe1[WR]); //close pipe 2 write since pipe 2 is not used at all
        close (pipe2[RD]); // close pipe 1 write, because nothing is being written only read
        dup2(pipe2[WR], 1); //redirect pipe 1 read to stdin to take "grep" output as input
        execlp ("ps", "ps", "-A", NULL); //execute word count
      }
      else
      {
        //grandchild
        close (pipe2[WR]); //pipe 2 is not being written to
        close (pipe1[RD]); // pipe 1 is not be read from
        dup2(pipe2[RD], 0); //redirect pipe 2 read to std in to take "ps" out put as input
        dup2(pipe1[WR], 1); //redirect pipe 1 write to std out to output grep command

        //execute grep onto the out of ps -a by using program argument as parameter
        execlp("grep", "grep", argv[1],NULL);
      }
    }
    else
    {
      //great-grandchild
      close (pipe2[RD]); //close pipe 1 read
      close (pipe2[WR]); //close pipe 1 write, because pipe 1 is not being used at all
      close(pipe1[WR]); //close pipe 2 read because nothing is being read only written
      dup2 (pipe1[RD], 0); //redirect pipe 2 write to std out to output ps command
      execlp("wc", "wc", "-l", NULL); //execute ps command listing all running processes
    }
  }
  else
  {
    //parent
    wait (&status); // wait for child to be done
  }
}
