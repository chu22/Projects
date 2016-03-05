/*
*
* C Shell Program
* Created by Lawrence Chu
* CS361 - Computer Systems
*
* This is a simple C shell that supports command line arguments and single file redirection.
* Arguments are parsed through white space, including the file redirection operators,
* and only one file can be redirected per command.
*
*/

#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <signal.h>

/* 
* helper function that parses command line arguments of the shell, including redirection
*/
int parse_arg(char *argv[128], char *io[2], char *com){
   int i=0;
   io[0]=NULL;
   argv[i]=strtok(com," \n\r\t");
   while(argv[i]!=NULL){
      i++;
      argv[i]=strtok(NULL," \n\r\t");
      if(argv[i]!=NULL&&(strcmp(argv[i],"<")==0||strcmp(argv[i],">")==0||strcmp(argv[i],">>")==0)){
	io[0]=argv[i];
	io[1]=strtok(NULL," \n\r\t");
	argv[i]=NULL;
      }
   }
   return i;
}

/*
* helper function that checks for redirection, and changes the necessary file descriptors if necessary
*/
void io_redirect(char *io[2]){
int fd;
if(io[0]==NULL){
   return;
}
char *fname=io[1];
if(strcmp(io[0],"<")==0){
   fd=open(fname,O_RDONLY);
   if(fd<0){
      perror(fname);
      exit(0);
   }
   dup2(fd,STDIN_FILENO);
   close(fd);
}
else if(strcmp(io[0],">")==0){
   fd=creat(fname,0644);
    if(fd<0){
      perror(fname);
      exit(0);
   }
   dup2(fd,STDOUT_FILENO);
   close(fd);
}
else{
   fd=open(fname,O_WRONLY|O_CREAT|O_APPEND,0644);
    if(fd<0){
      perror(fname);
      exit(0);
   }
   dup2(fd,STDOUT_FILENO);
   close(fd);
}
}

/*
* signal handlers, stops default action of ^C/^Z and prints out the signal instead
*/
void sig_handler(int signum){
if(signum==SIGINT){
   printf("\ncatch sigint\n");
}
if(signum==SIGTSTP){
   printf("\ncatch sigtstp\n");
}
printf("CS361 >");
fflush(stdout);
}

/*
* the main shell program
*/
int main(){
char *argv[128];
char *io_redir[2];
char buf[128];
int argc;
pid_t pid;
int status;
signal(SIGINT,sig_handler);                      //set signal handlers
signal(SIGTSTP,sig_handler);
while(1){
   printf("CS361 >");
   fgets(buf,128,stdin);                         //parse input
   argc=parse_arg(argv,io_redir,buf);
   if(argc==1&&strcmp(argv[0],"exit")==0){       //"exit" to exit shell
      return 0;
   }
   pid=fork();                                   //fork off new process
   if(pid<0){
      perror("fork failed");
   }
   else if(pid==0){
      io_redirect(io_redir);                     //do any io redirection
      execvp(argv[0],argv);                      //execute command line program
      perror(argv[0]);
      exit(0);
   }
   else{
      printf("pid: %d ",pid);
      waitpid(pid,&status,0);                    //wait for child process to terminate
      printf("status: %d\n",status);             //return exit status
   }
}
}
