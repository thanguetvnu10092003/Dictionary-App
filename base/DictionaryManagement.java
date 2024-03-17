package base;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class DictionaryManagement extends Dictionary{
    public static String getInputPath() {
        return INPUT_PATH;
    }

    public static void setInputPath(String inputPath) {
        INPUT_PATH = inputPath;
    }

    public static String getOutputPath() {
        return OUTPUT_PATH;
    }

    public static void setOutputPath(String outputPath) {
        OUTPUT_PATH = outputPath;
    }

    private static String INPUT_PATH = "";
    private static String OUTPUT_PATH = "";
    public static void insertFromCommandline() {
        Scanner sc_string  = new Scanner(System.in);
        Scanner sc_int  = new Scanner(System.in);
        System.out.println("Enter a number of words: ");
        int word_list_size = sc_int.nextInt();
        for (int i = 0; i < word_list_size; i++) {
            System.out.println("Enter a word: ");
            String input = sc_string.nextLine();
            System.out.println("Enter its meaning: ");
            String meaning = sc_string.nextLine();
            Word latest_word = new Word(input, meaning);
            oldWord.add(latest_word);
        }
    }

    public static void insertFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(INPUT_PATH)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] wordsInline = line.split("\t");
                Word new_word = new Word(wordsInline[0], wordsInline[1]);
                oldWord.add(new_word);
            }
            Collections.sort(oldWord);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportToFile(String OUTPUT_PATH) {
        try {
            OutputStream outputStream = new FileOutputStream(new File(OUTPUT_PATH));
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);
            for (Word word : oldWord) {
                writer.write(String.format("%-15s %-15s%n", word.getSearching(), word.getMeaning()));
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateWordtoFile() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(INPUT_PATH));
            for (Word word : oldWord) {
                bufferedWriter.write(word.getSearching() + "\t" + word.getMeaning() + "\n");
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * check if the word is in the dictionary.
     * it returns the position of the word.
     * in case mid_pos = 0, the word will be in 0, it returns-1 when word not in the dic.
     * in case mid_pos > 0, it returns the pos of the word, and returns -1 if the word already exist.
     */
    public static int binaryGetPos(int start, int end, String word) {
        if (end < start)
            return -1;
        int mid_pos = start + (end - start) / 2;
        int check_next = word.compareTo(oldWord.get(mid_pos).getSearching());
        // dictionary has only 1 word
        if (mid_pos == 0) {
            // the word is in the 0 position
            if (check_next < 0) {
                return 0;
            } else if (check_next > 0) { // word have pos greater than mid_pos
                return binaryGetPos(mid_pos + 1, end, word);
            } else { // word not in dictionary
                return -1;
            }
        } else {
            // select position previous the current word
            int check_previous = word.compareTo(oldWord.get(mid_pos - 1).getSearching());
            // if word > than the prev-mid-pos word and < the next-mid-pos word
            // => word == mid-pos word
            if (check_previous > 0 && check_next < 0) {
                return mid_pos;
                // word < prev-mid-pos word
            } else if (check_previous < 0) {
                return binaryGetPos(start, mid_pos - 1, word);
                // word > next-mid-pos word
            } else if (check_next > 0) {
                // word is in the last pos
                if (mid_pos == oldWord.size() - 1) {
                    return oldWord.size();
                }
                return binaryGetPos(mid_pos + 1, end, word);
            } else {
                return -1; // word already in dictionary
            }
        }
    }

    /**
     * add a word and its meaning.
     */
    public static void addWord(String input_word, String input_meaning) {
        input_word = input_word.toLowerCase();
        input_meaning = input_meaning.toLowerCase();
        int posInputWord = binaryGetPos(0, oldWord.size(), input_word);
        if (posInputWord == -1) {
            System.out.println("The word existed");
            return;
        }
        oldWord.add(new Word());
        for (int i = oldWord.size() - 2; i >= posInputWord; i--) {
            oldWord.get(i + 1).setSearching(oldWord.get(i).getSearching());
            oldWord.get(i + 1).setMeaning(oldWord.get(i).getMeaning());
        }
        oldWord.get(posInputWord).setSearching(input_word);
        oldWord.get(posInputWord).setMeaning(input_meaning);
        updateWordtoFile();
    }

    public static void alterWord(String altered_word, String altered_meaning) {
        altered_word = altered_word.toLowerCase();
        altered_meaning = altered_meaning.toLowerCase();

        int check = binaryGetPos(0, oldWord.size(), altered_word);
        if (check == -1) {
            int pos = Collections.binarySearch(oldWord, new Word(altered_word, null));
            oldWord.get(pos).setMeaning(altered_meaning);
        } else {
            System.out.println("Can't find the word you want to change");
        }
        updateWordtoFile();
    }

    public static void removeWord(String deleted_word) {
        deleted_word = deleted_word.toLowerCase();
        int check = binaryGetPos(0, oldWord.size(), deleted_word);
        if (check == -1) {
            int pos = Collections.binarySearch(oldWord, new Word(deleted_word, null));
            oldWord.remove(pos);
            System.out.println("Deleted success");
        } else {
            System.out.println("Can't the word you want to remove");
        }
        updateWordtoFile();
    }

    // find words that have the same element
    public List<Word> dictionarySearcher(String letter) {
        List<Word> foundWords = new ArrayList<>();
        for(Word word : oldWord) {
            if (word.getSearching().startsWith(letter)) {
                foundWords.add(word);
            }
        }
        return foundWords;
    }

    public static int isContain(String s1, String s2) {
        for(int i = 0; i < Math.min(s1.length(),s2.length()); i++) {
            if (s1.charAt(i) > s2.charAt(i)) {
                return 1;
            } else if (s1.charAt(i) < s2.charAt(i)) {
                return -1;
            }
        }
        if (s1.length() > s2.length()) {
            return 1;
        }
        return 0;
    }

    // search word position in dictionary
    public static int binaryLookup(int start, int end, String word) {
        if (end < start) return -1;
        int mid = start + (end - start) / 2;
        int compare = isContain(word, oldWord.get(mid).getSearching());
        // the word is on the left side
        if (compare == -1) return binaryLookup(start, mid - 1, word);
            // the word is on the right side
        else if (compare == 1) {
            return binaryLookup(mid + 1, end, word);
        }
        // the word is in the middle
        else return mid;
    }
//
//    public static void showWordLookup(String word, int index) {
//        // the word not in the dictionary
//        if (index < 0) return;
//
//        ArrayList<Word> listWord = new ArrayList<>();
//        int j = index;
//        while (j >= 0) {
//            if (isContain(word,oldWord.get(j).getSearching()) == 0) {
//                j--;
//            } else break;
//        }
//
//        for (int i = j + 1; i < index; i++) {
//            Word tmp = new Word(oldWord.get(i).getSearching(), oldWord.get(i).getMeaning());
//            listWord.add(tmp);
//        }
//
//        for (int i = index + 1; i < oldWord.size(); i++) {
//            if (isContain(word, oldWord.get(i).getSearching()) == 0) {
//                Word tmp = new Word(oldWord.get(i).getSearching(), oldWord.get(i).getMeaning());
//                listWord.add(tmp);
//            } else break;
//        }
//
//        for (Word w : listWord) {
//            System.out.println(w.getSearching());
//        }
//    }
//
//    public static void LookUpWord() throws IOException {
//        Scanner sc = new Scanner(System.in);
//        String w = sc.nextLine().toLowerCase();
//        int index = binaryLookup(0, oldWord.size(), w);
//        if (index < 0) {
//            index = binaryLookup(0, oldWord.size(), w);
//        }
//        showWordLookup(w,index);
//    }

    public static String findVietnameseMeaning(String word) {
        int index = binaryLookup(0, oldWord.size(), word.toLowerCase());
        if (index == -1) {
            return "Can't find the word you want to search!";
        }
        else
            return oldWord.get(index).getMeaning();
    }
}
