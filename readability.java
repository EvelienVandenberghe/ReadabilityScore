package readability;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class Main {


   static final int[] AGE_BRACKETS = {
           0,   // 0 - unused
           6,   // 1
           7,   // 2
           8,   // 3
           9,   // 4
           10,  // 5
           11,  // 6
           12,  // 7
           13,  // 8
           14,  // 9
           15,  // 10
           16,  // 11
           17,  // 12
           18,  // 13
           22   // 14+
   };


   public static void main(String[] args) throws IOException {
       String filePath = args[0];                            //1st arg (the file path)
       String text = new String(Files.readAllBytes(Paths.get(filePath)));


       System.out.println("The text is:");
       System.out.println(text);


       // ── Basic counts ──────────────────────────────────────────────────────


       // Characters: everything except space, newline, tab
       int characters = 0;
       for (char c : text.toCharArray()) {
           if (c != ' ' && c != '\n' && c != '\t' && c != '\r') {
               characters++;
           }
       }


       // Words
       String[] words = text.trim().split("\\s+");
       int wordCount = words.length;


       // Sentences
       String[] sentenceArr = text.split("[.!?]", -1);
       int sentenceCount = sentenceArr.length;
       if (sentenceArr[sentenceArr.length - 1].trim().isEmpty()) {
           sentenceCount--;
       }


       // Syllables and polysyllables
       int totalSyllables = 0;
       int polysyllables = 0;
       for (String word : words) {
           int s = countSyllables(word);
           totalSyllables += s;
           if (s > 2) polysyllables++;
       }


       System.out.println("Words: " + wordCount);
       System.out.println("Sentences: " + sentenceCount);
       System.out.println("Characters: " + characters);
       System.out.println("Syllables: " + totalSyllables);
       System.out.println("Polysyllables: " + polysyllables);


       // ── Score selection ───────────────────────────────────────────────────


       Scanner scanner = new Scanner(System.in);
       System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
       String choice = scanner.nextLine().trim();
       System.out.println();


       double ariAge = 0, fkAge = 0, smogAge = 0, clAge = 0;
       int methodsChosen = 0;


       boolean doARI  = choice.equals("ARI")  || choice.equals("all");
       boolean doFK   = choice.equals("FK")   || choice.equals("all");
       boolean doSMOG = choice.equals("SMOG") || choice.equals("all");
       boolean doCL   = choice.equals("CL")   || choice.equals("all");


       if (doARI) {
           double score = 4.71 * ((double) characters / wordCount)
                   + 0.5  * ((double) wordCount / sentenceCount)
                   - 21.43;
           ariAge = getAge(score);
           System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).%n",
                   score, (int) ariAge);
           methodsChosen++;
       }


       if (doFK) {
           double score = 0.39 * ((double) wordCount / sentenceCount)
                   + 11.8 * ((double) totalSyllables / wordCount)
                   - 15.59;
           fkAge = getAge(score);
           System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).%n",
                   score, (int) fkAge);
           methodsChosen++;
       }


       if (doSMOG) {
           double score = 1.043 * Math.sqrt(polysyllables * (30.0 / sentenceCount)) + 3.1291;
           smogAge = getAge(score);
           System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).%n",
                   score, (int) smogAge);
           methodsChosen++;
       }


       if (doCL) {
           double L = (double) characters / wordCount * 100;
           double S = (double) sentenceCount / wordCount * 100;
           double score = 0.0588 * L - 0.296 * S - 15.8;
           clAge = getAge(score);
           System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds).%n",
                   score, (int) clAge);
           methodsChosen++;
       }


       // ── Average age (only shown when "all" is selected) ───────────────────
       if (choice.equals("all")) {
           double avgAge = (ariAge + fkAge + smogAge + clAge) / methodsChosen;
           System.out.printf("%nThis text should be understood in average by %.2f-year-olds.%n", avgAge);
       }
   }


   // ── Syllable counting ─────────────────────────────────────────────────────


   static int countSyllables(String word) {
       // Strip punctuation so "pages." doesn't confuse the count
       word = word.toLowerCase().replaceAll("[^a-z]", "");


       if (word.isEmpty()) return 1;


       // Rule 3: remove trailing 'e'
       if (word.endsWith("e") && word.length() > 1) {
           word = word.substring(0, word.length() - 1);
       }


       // Rule 1 & 2: count vowel groups (consecutive vowels = 1 syllable)
       int syllables = 0;
       boolean prevWasVowel = false;
       for (char c : word.toCharArray()) {
           boolean isVowel = "aeiouy".indexOf(c) >= 0;
           if (isVowel && !prevWasVowel) {                      // Only count if it's a vowel AND previous wasn't
               syllables++;
           }
           prevWasVowel = isVowel;
       }


       // Rule 4: every word has at least 1 syllable
       return Math.max(syllables, 1);
   }


   // ── Age lookup ────────────────────────────────────────────────────────────


   static double getAge(double score) {
       int index = (int) Math.ceil(score);
       index = Math.min(Math.max(index, 1), 14);
       return AGE_BRACKETS[index];
   }
}
