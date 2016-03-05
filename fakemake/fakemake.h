/*
* fakemake program
* Lawrence Chu
* CS251 - Data Structures
* 
* This project emulates the makefile functionality for
* building files in C. The fakemake.c program holds the 
* data structures and functions for the project.
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "hmap.h"

typedef struct fake_make_graph GRAPH;

/*
* frees graph struct
*/
extern void free_graph(GRAPH *g);

/*
* creates a graph from a file
*/
extern GRAPH *g_from_file(char *fname);

/*
* returns true if the graph contains a cycle,
*/
extern int check_cycle(GRAPH *g);

/*
* sets the updated timestamp on a particular file
*/
extern void set_time(GRAPH *g, char *fname, int t);


/*
* gets last updated timestamp on a file
*/
extern int get_time(GRAPH *g, char *fname);

/*
* prints all timestamps for all files in the fakemake
*/
extern void print_times(GRAPH *g);

/*
* conducts a depth first search on the graph starting at a file
*/
extern int make_dfs(GRAPH *g, char *fname, int t);
