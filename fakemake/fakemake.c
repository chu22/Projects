/*
* fakemake.c
* Lawrence Chu
* CS251 - Data Structures
* 
* A program that emulates the wokrings of a makefile
* using hashmaps and graphs
*
*/

#include "fakemake.h"

typedef struct dependency{
char *name;
struct dependency *next;
}NODE;

typedef struct program_file{
int id;
int time;
NODE *depend;
}PFILE;

struct fake_make_graph{
HMAP_PTR files;
NODE *fnames;
int n;
};

printG(GRAPH *g){
int i;
NODE *d=g->fnames;
NODE *e;
PFILE *p;
printf("size = %d",g->n);
for(i=0;i<g->n;i++){
   printf("name = %s\n",d->name);
   p=hmap_get(g->files,d->name);
   printf("id = %d, time = %d\n",p->id,p->time);
   e=p->depend;
   printf("dep names: ");
   while(e!=NULL){
      printf("%s ",e->name);
      e=e->next;
   }
   printf("\n");
   d=d->next;
}
}

void free_dep(NODE *d){
if(d==NULL){
   return;
}
free(d->name);
free_dep(d->next);
free(d);
}

void free_graph(GRAPH *g){
int i;
NODE *p=g->fnames;
PFILE *q;
for(i=0;i<g->n;i++){
   q=hmap_get(g->files,p->name);
   free_dep(q->depend);
   p=p->next;
}
free_dep(g->fnames);
hmap_free(g->files,0);
free(g);
}

GRAPH *g_from_file(char *fname){
FILE *fp = fopen(fname, "r");
if(fp==NULL) {
   fprintf(stderr, "file %s does not exist\n");
   return NULL;
}
PFILE *f;
char *key;
NODE *d;
NODE *tmp;
GRAPH *g=malloc(sizeof(GRAPH));
g->n=0;
g->files=hmap_create(0,0.75);
g->fnames=NULL;
char buf[1000];
char *str;
while(fgets(buf,1000,fp)!=NULL){
   key=strtok(buf," \n\r\t");
   if(key==NULL){
      fprintf(stderr,"invalid empty line\n");
      return NULL;
   }
   f=malloc(sizeof(PFILE));
   f->id=g->n++;
   f->time=0;
   f->depend=NULL;
   tmp=malloc(sizeof(NODE));
   str=malloc(strlen(key)*sizeof(char));
   strcpy(str,key);
   tmp->name=str;
   tmp->next=g->fnames;
   g->fnames=tmp;
   if(hmap_set(g->files,key,f)!=NULL){
      fprintf(stderr,"fakefile contains duplicate file names\n");
      free_graph(g);
      return NULL;
   }
   key=strtok(NULL," \n\r\t");
   if(key!=NULL){
      if(strcmp(key,":")==0){
	key=strtok(NULL," \n\r\t");
	if(key!=NULL){
	   while(key!=NULL){
	      tmp=f->depend;
	      d=malloc(sizeof(NODE));
	      str=malloc(strlen(key)*sizeof(char));
	      strcpy(str,key);
	      d->name=str;
	      d->next=tmp;
	      f->depend=d;
	      key=strtok(NULL," \n\r\t");
	   }
	   break;
	}
      }
      else{
	fprintf(stderr,"file name cannot contain whitespace\n");
	free_graph(g);
	return NULL;
      }
   }	   
}
while(fgets(buf,1000,fp)!=NULL){
   key=strtok(buf," \n\r\t");
   if(key==NULL){
      fprintf(stderr,"invalid empty line\n");
      return NULL;
   }
   f=malloc(sizeof(PFILE));
   f->id=g->n++;
   f->time=0;
   f->depend=NULL;
   tmp=malloc(sizeof(NODE));
   str=malloc(strlen(key)*sizeof(char));
   strcpy(str,key);
   tmp->name=str;
   tmp->next=g->fnames;
   g->fnames=tmp;
   if(hmap_set(g->files,key,f)!=NULL){
      fprintf(stderr,"fakefile contains duplicate file names\n");
      free_graph(g);
      return NULL;
   }
   key=strtok(NULL," \n\r\t");
   if(key==NULL){
	fprintf(stderr,"all basic files must be listed before target files\n");
	free_graph(g);
	return NULL;
   }
   if(strcmp(key,":")!=0){
	fprintf(stderr,"file name cannot contain whitespace\n");
	free_graph(g);
	return NULL;
   } 
   key=strtok(NULL," \n\r\t");
   if(key==NULL){
	fprintf(stderr,"all basic files must be listed before target files\n");
	free_graph(g);
	return NULL;
   }
   while(key!=NULL){
      tmp=f->depend;
      d=malloc(sizeof(NODE));
      str=malloc(strlen(key)*sizeof(char));
      strcpy(str,key);
      d->name=str;
      d->next=tmp;
      f->depend=d;
      key=strtok(NULL," \n\r\t");
   }
}
fclose(fp);
return g;
}


void dfs_r(GRAPH *g, char *color, PFILE *p, int *cycle){
NODE *d;
PFILE *q;
color[p->id] = 'G';
d=p->depend;
while(d != NULL) {
   if(*cycle==1){
      return;
   }
   q = hmap_get(g->files,d->name);
   if(q==NULL){
      fprintf(stderr,"invalid dependency in fakefile\n");
      *cycle = 1;
      return;
   }
   if(color[q->id] == 'W') {
      dfs_r(g,color,q,cycle);
   }
   else if(color[q->id] == 'G') {
      fprintf(stderr,"fakefile contains cycle\n");
      *cycle = 1;
      return;
   }
   d = d->next; 
}
color[p->id] = 'B';
}

int check_cycle(GRAPH *g) {
int s;
int cycle=0;
char *color = malloc(g->n*sizeof(char));
NODE *d = g->fnames;
PFILE *p;
for(s=0;s<g->n;s++){
   color[s]='W';
}
for(s=g->n-1; s>=0; s--) {
   if(color[s] == 'W'){
      p=hmap_get(g->files,d->name);
      dfs_r(g,color,p,&cycle);
      if(cycle){
	return 1;
      }
   }
   d=d->next;
}
return 0;
}

void set_time(GRAPH *g, char *fname, int t){
PFILE *f = hmap_get(g->files,fname);
if(f==NULL){
   fprintf(stderr,"invalid file name\n");
   return;
}
printf("file '%s' has been modified\n",fname);
f->time=t;
}

int get_time(GRAPH *g, char *fname){
PFILE *f = hmap_get(g->files,fname);
if(f==NULL){
   fprintf(stderr,"invalid file name\n");
   return -9;
}
printf("%d\n",f->time);
return f->time;
}

void print_times(GRAPH *g){
int i;
PFILE *f;
NODE *p=g->fnames;
for(i=0;i<g->n;i++){
   f = hmap_get(g->files,p->name);
   printf("%s: %d\n",p->name,f->time);
   p=p->next;
}
}

int make_dfs_r(GRAPH *g, int *t, PFILE *p, char *color, char *fname){
if(p->depend==NULL){
   color[p->id]='B';
   printf("%s is up to date\n",fname);
   return p->time;
}
int cur,max=0;
NODE *d;
PFILE *q;
d = p->depend;
int i;
while(d!=NULL){
   q = hmap_get(g->files,d->name);
   if(color[q->id]=='B'){
      cur = q->time;
   }
   else{
      cur = make_dfs_r(g,t,q, color,d->name);
   }
   if(max<cur){
      max=cur;
   }
   d = d->next;
}
if(p->time<max){
   printf("making %s...done\n",fname);
   p->time=(*t)++;
   
}
else{
   printf("%s is up to date\n",fname);
}
color[p->id]='B';
return p->time;   
}

int make_dfs(GRAPH *g, char *fname, int t){
PFILE *p=hmap_get(g->files,fname);
if(p==NULL){
   fprintf(stderr,"invalid file name\n");
   return t;
}
char *color=malloc(g->n*sizeof(char));
int i;
for(i=0;i<g->n;i++){
   color[i]='W';
}
int s=make_dfs_r(g,&t,p,color,fname);
return s>t?s:t;
}


