Notes:
	Make sure to first compile both the mainCPU.java and the memory.java
	- "javac mainCPU.java memory.java"
	Both the processors are required to run this project.
	Memory is used to read the files and store values, while the mainCPU 
	is used to execute all the instructions/functions.
	When typing in the termimal to execute each text file. 
	Type as follows:
	- "java mainCPU sampleN.txt 30"
	- For the 3 input values
		- mainCPU program
		- the sample text files
		- integer timer for interrupt 
	- Replace N with the values 1-5 to run each text file.

sample1.txt 
	This would index the load instructions and print two tables, one is 
	the alphabet and the other being the integers 1-10.
	Expected output for sample1.txt:
	ABCDEFGHIJKLMNOPQRSTUVWXYZ12345678910

sample2.txt
	This would call and return instructions which would output the smiley
	face as shown.
	Expected output for sample5.txt:
	    ------
	 /         \
	/   -*  -*  \
	|           |
	\   \____/  /
	 \         /
	    ------

sample3.txt
	This would call and interrupt return instructions. For this case a loop 
	would be used to print the letter A with an integer (timer) that has 
	been incremented. With a shorter period timer, the integer output would
	be faster.
	Expected output for sample5.txt
	A
	0
	A
	0
	A
	2
	A
	4
	A
	5
	A
	7
	A
	9
	A
	10
	A
	12
	A
	14

sample4.txt
	This would display the user stack as well as the system stack in a way
	that would represent the system memory being accessed in the user mode
	and proceed to give an error as it exits.
	Expected output for sample5.txt
	1000
	999
	1000
	1994
	1993
	1994
	Memory violation: Accessing system address 1000 in user mode.

sample5.txt
	This would index and load instructions to draw out "Tina" within three
	lines.
	Expected output for sample5.txt
	_____
	  |* _  _
	  ||| |(_|_