This project was written by Lawrence Chu for CS251 Data Structures at UIC.
The program emulates a makefile, using graph and hashmap data strutures.
Users run the program with the name of a "fakefile" as an argument, which will load the file into a graph.
Then users can make the files within the fakefile with the make command, as well as other things such as
check timestamps of files and touch them to update the time stamps. The make command will ensure that all 
the dependencies of a particular file are up to date, and then update the file if needed.
The "time" is a simple counter that starts at time 0 and increments by 1 as updates are needed.
The format of the fakefiles are simply a list of strings with : to separate files from their dependencies.
All base files with no dependencies are to be listed first, and then files with dependencies are after.
Fakemake will error check for files that depend on each other, and ensure the fakefile is valid before allowing
the program to run.
A makefile for this project is included, and the project can be built by typing make fakemake.
Example fakefiles are also included to see formatting. 