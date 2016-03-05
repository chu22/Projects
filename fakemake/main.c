/*
* fakemake main program
* Lawrence Chu
* CS251 - Data Structures
* 
* the main program that takes in user commands
* and gives the proper information back to the 
* user.
*
*/

#include "fakemake.h"

int main(int argc, char *argv[]){
if(argc!=2){
   printf("incorrect command line arguments\n");
   return 0;
}
GRAPH *g=g_from_file(argv[1]);
if(g==NULL){
   fprintf(stderr,"Error in reading input file\n");
   return 0;
}
if(check_cycle(g)){
   return 0;
}
char buf[50];
char com[10];
char fl[45];
int t=1;
fgets(buf,50,stdin);
int nargs=sscanf(buf,"%s %s",com,fl);
while(strcmp(com,"quit")!=0){
   if(strcmp(com,"time")==0&&nargs==1){
      printf("%d\n",t);
   }
   else if(strcmp(com,"touch")==0&&nargs==2){
      set_time(g,fl,t);
      t++;
   }
   else if(strcmp(com,"timestamp")==0&&nargs==2){
      get_time(g,fl);
   }
   else if(strcmp(com,"timestamps")==0&&nargs==1){
      print_times(g);
   }
   else if(strcmp(com,"make")==0&&nargs==2){
      t=make_dfs(g,fl,t);
   }
   else{
      printf("invalid command\n");
   }
   fgets(buf,50,stdin);
   nargs=sscanf(buf,"%s %s",com,fl);
}
free_graph(g);
return 0;
}
