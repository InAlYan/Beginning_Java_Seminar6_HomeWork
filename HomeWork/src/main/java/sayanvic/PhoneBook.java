package sayanvic;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class PhoneBook {

    private HashMap<String, ArrayList<String>> phoneBook = new HashMap<>();
    private ArrayList<String> arrLst;

    private static Scanner scanner = new Scanner(System.in);
    public static String correctNameSymbols = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    public static String correctPhoneSymbols = " 0123456789-";

    public PhoneBook() {
    }

    public PhoneBook(String fileName) {
        loadFromFile(fileName);
    }

    private static String correctString(int minLen, int maxLen, String prompt, String correctSymbolsString) {
        String res = "";
        correctSymbolsString = correctSymbolsString.toLowerCase();
        while (res.length() < minLen || res.length() > maxLen || !isCorrectSetOfSymbols(res, correctSymbolsString)) {
            System.out.print(prompt);
            res = scanner.nextLine().toLowerCase().trim();
        }
        return res.substring(0, 1).toUpperCase() + res.substring(1);
    }

    /**
     * Проверка на допустимые символы в строке
     * @param checkedString - проверяемая строка
     * @param inspectorString - набор допустимы символов строкой
     * @return истина - если все символы допустимы, ложь если хотя бы 1 символ недопустим
     */
    private static boolean isCorrectSetOfSymbols(String checkedString, String inspectorString) {

        char[] arrCharChecked = checkedString.toCharArray();
        Character[] arrChecked = IntStream.range(0, arrCharChecked.length).mapToObj(i -> arrCharChecked[i]).toArray(Character[]::new);
        HashSet<Character> checked = new HashSet<>(List.of(arrChecked));

        char[] arrCharInspector = inspectorString.toCharArray();
        Character[] arrInspector = IntStream.range(0, arrCharInspector.length).mapToObj(i -> arrCharInspector[i]).toArray(Character[]::new);
        HashSet<Character> inspector = new HashSet<>(List.of(arrInspector));

        checked.removeAll(inspector);
        return checked.isEmpty();
    }

    /**
     * Ввести телефон в корректном формате
     *
     * @return строку в корректном формате телефон (89139998877 или 8-913-999-88-77 или 333)
     */
    public static String inputPhone() {
        return correctString(3, 15, "Введите корректный номер телефона (от 3 до 15 символов включая цифры, пробел и минус): ",
                correctPhoneSymbols);
    }

    /**
     * Ввести корректное полное имя включающее фамилию, имя и отчество
     *
     * @return строку в формате фамилия + имя + отчество
     */
    public static String inputFullName() {
        String name = correctString(2, 15, "Введите корректное имя (не мене 2 и не более 15 символов): ", correctNameSymbols);
        String patronim = correctString(4, 15, "Введите корректное отчество (не мене 4 и не более 15 символов): ", correctNameSymbols);
        String surname = correctString(1, 25, "Введите корректную фамилию (не мене 1 и не более 25 символов): ", correctNameSymbols + "-");
        return surname + " " + name + " " + patronim;
    }

    /**
     * Добавить в телефонную книгу новую запись интерактивно
     *
     * @return строку с добавленными данными в формате ФИО : телефон
     */
    public String add() {
        return addToPhoneBook(inputFullName(), inputPhone());
    }

    private String addToPhoneBook(String fio, String phoneNumber) {
        arrLst = (phoneBook.containsKey(fio)) ? phoneBook.get(fio) : new ArrayList<>();
        arrLst.add(phoneNumber);
        phoneBook.put(fio, arrLst);
        return fio + " : " + phoneNumber;
    }

    /**
     * Удалить запись из телефонной книги по фамилии
     *
     * @param fio - фамилия, имя, отчество в корректном формате
     * @return строка результата: фамилия, имя, отчество и список его телефонов или строка "запись не найдена"
     */
    public String deleteFIO(String fio) {
        arrLst = phoneBook.remove(fio);
        return (arrLst != null) ? fio + " " + arrLst : "запись " + fio + " не найдена...";
    }

    /**
     * Удалить телефон у владельца
     *
     * @param fio - фамилия, имя, отчество в корректном формате
     * @param phoneNumber - телефон в корректном формате
     * @return строка результата
     */
    public String deletePhoneByFIO(String fio, String phoneNumber) {
        arrLst = phoneBook.get(fio);
        return (arrLst != null) ? (arrLst.remove(phoneNumber)) ? "телефон " + phoneNumber + " удален у " + fio :
                "телефон " + phoneNumber + " не найден у " + fio :
                "телефон " + phoneNumber + " не найден у несуществующего " + fio;
    }

    /**
     * Вернуть все телефоны по фамилии
     *
     * @param fio фамилия, имя, отчество, в корректном формате
     * @return строку всех телефонов или ""
     */
    public String phones(String fio) {
        return (phoneBook.containsKey(fio)) ? phoneBook.get(fio).toString() : "";
    }

    /**
     * Вернуть владельца(ов) по номеру телефона
     *
     * @param phoneNumber - телефон в корректном формате
     * @return строку владельцев данного телефона или ""
     */
    public String owners(String phoneNumber) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ArrayList<String>> curRecord : phoneBook.entrySet()) {
            arrLst = curRecord.getValue();
            if (arrLst.contains(phoneNumber)) sb.append(curRecord.getKey()).append(", ");
        }
        String outStr = sb.toString();
        return (outStr.isEmpty()) ? "" : outStr.substring(0, outStr.length() - 2);
    }

    /**
     * Загрузить телефонную книгу из файла
     *
     * @param fileName - имя файла с телефонной книгой для загрузки
     */
    public void loadFromFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        File f = new File(fileName);
        try {
            f.createNewFile();
            FileReader fr = new FileReader(f);
            while (fr.ready()) {
                int c = fr.read();
                if ((char) c == '\n') {
                    String[] parts = sb.toString().split(";");
                    for (int i = 1; i < parts.length; i++) addToPhoneBook(parts[0], parts[i]);
                    sb.setLength(0);
                } else {
                    sb.append((char) c);
                }
            }
        } catch (IOException e) {
            System.out.println("Не удалось удалось загрузить справочник из файла: " + e.getMessage());
        }
    }

    /**
     * Сохранить телефонную книгу в файл
     *
     * @param fileName - имя файла с телефонной книгой для сохранения
     */
    public void saveToFile(String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            for (Map.Entry<String,ArrayList<String>> curRecord : phoneBook.entrySet()) {
                StringBuilder sb = new StringBuilder(curRecord.getKey());
                sb.append(";");
                arrLst = curRecord.getValue();
                for (String curPhone : arrLst) sb.append(curPhone).append(";");
                sb.append("\n");
                fw.write(sb.toString());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Не удалось сохранить справочник в файл: " + e.getMessage());
        }
    }

    /**
     * Преобразовать к строке телефонный справочник отсортированный по убыванию числа телефонов
     * @return преобразованный к строке телефонный справочник отсортированный по убыванию числа телефонов
     */
    private String showByAscendingCountOfPhones() {

        // Создаем и наполняем TreeMap sortedPhoneBook данными из HashMap phoneBook, где ключом будет количество телефонов,
        // а значением ArrayList фамилий
        TreeMap<Integer, ArrayList<String>> sortedPhoneBook = new TreeMap<>(Comparator.reverseOrder());
        for (Map.Entry<String,ArrayList<String>> curRecord : phoneBook.entrySet()) {
            int countOfPhones = curRecord.getValue().size();
            arrLst = (sortedPhoneBook.get(countOfPhones) != null) ? sortedPhoneBook.get(countOfPhones) : new ArrayList<>();
            arrLst.add(curRecord.getKey());
            sortedPhoneBook.put(countOfPhones, arrLst);
        }

        // Формируем строку из отсортированного TreeMap sortedPhoneBook, где сначала проходим по sortedPhoneBook берем
        // его значение ArrayList с фамилиями и обходя его (ArrayList) в цикле используем значения этих фамилий как ключи
        // для получения телефонов уже из HashMap phoneBook
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer,ArrayList<String>> curRecord : sortedPhoneBook.entrySet()) {
            arrLst = curRecord.getValue();
            for (String fio : arrLst) {
                sb.append(fio).append("\n");
                ArrayList<String> pnones = phoneBook.get(fio);
                for (String curPhone : pnones) sb.append(">>> ").append(curPhone).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return showByAscendingCountOfPhones();
    }

}
