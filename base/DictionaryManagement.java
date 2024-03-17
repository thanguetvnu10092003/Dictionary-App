package base;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Scanner;
import java.util.SimpleTimeZone;

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
        int word_list_size = sc_int.nextInt();
        for (int i = 0; i < word_list_size; i++) {
            String input = sc_string.nextLine();
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

    public static int binarySearch(int start, int end, String word) {
        if (end < start)
            return -1;
        int mid_pos = start + (end - start) / 2;
        int check_next = word.compareTo(oldWord.get(mid_pos).getSearching());
        if (mid_pos == 0) {
            if (check_next < 0) {
                return 0;
            } else if (check_next > 0) {
                return binarySearch(mid_pos + 1, end, word);
            } else {
                return -1;
            }
        } else {
            int check_previous = word.compareTo(oldWord.get(mid_pos - 1).getSearching());
            if (check_previous > 0 && check_next < 0) {
                return mid_pos;
            } else if (check_previous < 0) {
                return binarySearch(start, mid_pos - 1, word);
            } else if (check_next > 0) {
                if (mid_pos == oldWord.size() - 1) {
                    return oldWord.size();
                }
                return binarySearch(mid_pos + 1, end, word);
            } else {
                return -1;
            }
        }
    }
    public static void addWord(String input_word, String input_meaning) {
        input_word = input_word.toLowerCase();
        input_meaning = input_meaning.toLowerCase();
        int posInputWord = binarySearch(0, oldWord.size(), input_word);
        if (posInputWord == -1) {
            System.out.println("Từ đã tồn tại");
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
}
