# Readability Score

Readability Score Calculator:  
A Java program that analyzes text complexity using multiple readability formulas to determine what age group can understand it.  

### Features
Calculates four readability metrics:  

ARI (Automated Readability Index)  
FK (Flesch-Kincaid)  
SMOG (Simple Measure of Gobbledygook)  
CL (Coleman-Liau Index)  

Each score maps to an age group (6-22 years old).  

The program will:  

Display the text  
Show word/sentence/character/syllable counts  
Ask which score to calculate (or "all")  
Output readability scores with corresponding age levels  

### How It Works  
  
Syllables: Counts vowel groups, removes silent 'e'  
Polysyllables: Words with 3+ syllables  
Formulas: Each uses different combinations of word length, sentence length, syllables, and characters  
Age mapping: Scores are converted to reading age using a lookup table  
  
Input Format  
Plain text file passed as command-line argument. Sentences should end with ., !, or ?.  
