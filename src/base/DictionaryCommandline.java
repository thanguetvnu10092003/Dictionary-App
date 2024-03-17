package base;

import java.util.Scanner;

public class DictionaryCommandline extends Dictionary{
    public static void showAllWords() {
        System.out.printf("%-6s%c %-15s%c %-20s%n","No", '|' ,"English", '|', "Vietnamese");
        for (int i = 0; i < oldWord.size(); i++) {
            System.out.printf("%-6d%c %-15s%c %-15s%n",i + 1, '|', oldWord.get(i).getSearching(), '|', oldWord.get(i).getMeaning());
        }
    }

    public static void dictionaryAdvance() {
        boolean running = true;
        while (running) {
            System.out.println("Welcome to My Application!\n" +
                    "[0] Exit\n" +
                    "[1] Add\n" +
                    "[2] Remove\n" +
                    "[3] Update\n" +
                    "[4] Display\n" +
                    "[5] Lookup\n" +
                    "[6] Search\n" +
                    "[7] Game\n" +
                    "[8] Import from file\n" +
                    "[9] Export to file\n" +
                    "Your action:");
            Scanner input = new Scanner(System.in);
            int input_num = input.nextInt();
            switch (input_num) {
                case 0:
                    System.out.println("Shutting down");
                    running = false;
                    break;
                case 1:
                    Scanner sc1 = new Scanner(System.in);
                    String new_word = sc1.nextLine();
                    String word_meaning = sc1.nextLine();
                    DictionaryManagement.addWord(new_word, word_meaning);
                    break;
                case 2:
//                    Scanner sc2 = new Scanner(System.in);
//                    String deleted_word = sc2.nextLine();
//                    DictionaryManagement.removeWord(deleted_word);
                    break;
                case 3:
//                    Scanner sc3 = new Scanner(System.in);
//                    String altered_word = sc3.nextLine();
//                    String altered_meaning = sc3.nextLine();
//                    DictionaryManagement.alterWord(altered_word, altered_meaning);
                    break;
                case 4:
                    showAllWords();
                    break;
                case 5:

                    break;
                case 6:
//                    Scanner sc5 = new Scanner(System.in);
//                    String searched_word = sc5.nextLine();
//                    DictionaryManagement.searchWord(searched_word);
                    break;
                case 7:
                    break;
                case 8:
                    System.out.println("Import a path of a file:");
                    Scanner sc8 = new Scanner(System.in);
                    String input_directory = sc8.nextLine();
                    DictionaryManagement.setInputPath(input_directory);
                    DictionaryManagement.insertFromFile();
                    break;
                case 9:
                    System.out.println("Create a path which contains the list of words from dictionary:");
                    Scanner sc9 = new Scanner(System.in);
                    String output_directory = sc9.nextLine();
                    DictionaryManagement.setOutputPath(output_directory);
                    DictionaryManagement.exportToFile(output_directory);
                    break;
                default:
                    System.out.println("Action not supported!!!");
            }
        }
    }
}
