package base;

import java.io.*;
import java.util.*;

public class DictionaryManagement extends Dictionary{
    private static String INPUT_PATH = "src/base/default.txt";
    private static String PREV_INPUT_PATH = "";
    private static String OUTPUT_PATH = "";
    public static String getInputPath() {
        return INPUT_PATH;
    }

    public static void setInputPath(String inputPath) {
        PREV_INPUT_PATH = INPUT_PATH;
        INPUT_PATH = inputPath;
    }

    public static String getOutputPath() {
        return OUTPUT_PATH;
    }

    public static void setOutputPath(String outputPath) {
        OUTPUT_PATH = outputPath;
    }

    public static void insertFromCommandline() {
        Scanner sc_string  = new Scanner(System.in);
        Scanner sc_int  = new Scanner(System.in);
        System.out.println("Enter a number of words: ");
        int word_list_size = sc_int.nextInt();
        for (int i = 0; i < word_list_size; i++) {
            System.out.println("Enter a word: ");
            String input = sc_string.nextLine();
            if (!oldWord.isEmpty()) {
                int check = binaryGetPos(0, oldWord.size(), input);
                if (check != -1) {
                    System.out.println("Enter its meaning: ");
                    String meaning = sc_string.nextLine();
                    Word latest_word = new Word(input, meaning);
                    oldWord.add(latest_word);
                } else {
                    System.out.println("The word already exist");
                    break;
                }
            } else {
                System.out.println("Enter its meaning: ");
                String meaning = sc_string.nextLine();
                Word latest_word = new Word(input, meaning);
                oldWord.add(latest_word);
            }
        }
        Collections.sort(oldWord);
    }

    public static void removeDuplicates() {
        Set<Word> uniqueWord = new HashSet<>();
        List<Word> nonDuplicatesWord = new ArrayList<>();

        for (Word w : oldWord) {
            if (!uniqueWord.contains(w)) {
                uniqueWord.add(w);
                nonDuplicatesWord.add(w);
            }
        }

        oldWord.clear();
        oldWord.addAll(nonDuplicatesWord);
    }

    public static void insertFromFile() {
        try {
            BufferedReader reader1 = new BufferedReader(new FileReader(new File(PREV_INPUT_PATH)));
            String line1;
            while ((line1 = reader1.readLine()) != null) {
                String[] wordsInline = line1.split("\t");
                if (wordsInline.length >= 2) {
                    Word new_word = new Word(wordsInline[0], wordsInline[1]);
                    oldWord.add(new_word);
                }
            }

            BufferedReader reader = new BufferedReader(new FileReader(new File(INPUT_PATH)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] wordsInline = line.split("\t");
                if (wordsInline.length >= 2) {
                    Word new_word = new Word(wordsInline[0], wordsInline[1]);
                    oldWord.add(new_word);
                }
            }
            Collections.sort(oldWord);
            removeDuplicates();
            updateWordtoFile();
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Custom binary search.
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
        if (oldWord.size() > 2) {
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
        } else {
            Word word = new Word(input_word, input_meaning);
            oldWord.add(word);
        }
        Collections.sort(oldWord);
        updateWordtoFile();
    }

    /**
     * change a word's meaning.
     */
    public static void alterWord(String altered_word, String altered_meaning) {
        altered_word = altered_word.toLowerCase();
        altered_meaning = altered_meaning.toLowerCase();
        int pos = -1;
        pos = Collections.binarySearch(oldWord, new Word(altered_word, null));
        if (pos >= 0) {
            oldWord.get(pos).setMeaning(altered_meaning);
        } else {
            System.out.println("Can't find the word you want to change");
        }
        updateWordtoFile();
    }

    /**
     * remove a word from dictionary.
     */
    public static void removeWord(String deleted_word) {
        deleted_word = deleted_word.toLowerCase();
        int index = Collections.binarySearch(oldWord, new Word(deleted_word, null));
        System.out.println(index);
        if (index >= 0) {
            oldWord.remove(oldWord.get(index));
            System.out.println("Deleted success");
        } else {
            System.out.println("Can't find the word you want to remove");
        }
        updateWordtoFile();
    }

    /**
     * find words that have similar letter.
     */
    public List<Word> dictionarySearcher(String letter) {
        List<Word> foundWords = new ArrayList<>();
        for(Word word : oldWord) {
            if (word.getSearching().startsWith(letter)) {
                foundWords.add(word);
            }
        }
        return foundWords;
    }

    /**
     * show word's meaning
     */
    public static String findVietnameseMeaning(String word) {
        int index = Collections.binarySearch(oldWord, new Word(word, null));
        if (index == -1) {
            return "Can't find the word you want to search!";
        }
        else
            return oldWord.get(index).getMeaning();
    }
}