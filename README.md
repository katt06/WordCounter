# WordCounter
Java program that counts word occurrences in a given input file and outputs an HTML document with a table of the words and counts listed in alphabetical order

# Features
- Reads any .txt input file
- Counts how many times each word appears
- Ignores punctuation and treats words case-insensitively
- Outputs a simple, clean HTML file with a heading showing the input file name and a table of words sorted alphabetically alongside their counts

# Project Structure
WordCounter.java: Main class containing all logic and HTML output functionality
Uses OSU CSE Components - Link to OSU API: https://cse22x1.engineering.osu.edu/common/doc/

# How it works
1. Prompts the user for the name of the input text file and the name of the output HTML file
2. Scans the input file line-by-line
3. Splits text into words using a custom set of separator characters
4. Counts each unique word using a Map
5. Sorts all words alphabetically
6. Outputs a styled HTML file showing the word frequency table

# Installation
Prereqs: 
  - Java Development Kit (JDK)
  - OSU CSE Components Library
Run Instructions:
  - Compile: javac WordCounter.java
  - Run: java WordCounter
  - Follow Prompts to enter the name of the input file (e.g., sample.txt) and the output HTML file path (e.g., output.html)

# License
This project is from the CSE 2221 course at The Ohio State University.
