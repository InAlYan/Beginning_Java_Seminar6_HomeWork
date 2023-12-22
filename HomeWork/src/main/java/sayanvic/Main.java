package sayanvic;

// Реализуйте структуру телефонной книги с помощью HashMap.
// Программа также должна учитывать, что во входной структуре будут повторяющиеся имена с разными телефонами,
// их необходимо считать, как одного человека с разными телефонами.
// Вывод должен быть отсортирован по убыванию числа телефонов.

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String fileName = "phonebook.txt", fullName, phoneToFind;
        Scanner scanner = new Scanner(System.in);
        boolean f = true;

        PhoneBook phoneBook = new PhoneBook(fileName);

        while (f) {
            showMenu();
            String userInput = scanner.nextLine();
            if (!userInput.isEmpty()) {
                switch (userInput.charAt(0)) {
                    case '0':
                        f = false;
                        System.out.println("Завершение работы телефонного справочника...");
                        phoneBook.saveToFile(fileName);
                        break;
                    case '1':
                        System.out.println("Добавление записи:");
                        System.out.println("Добавление записи: " + phoneBook.add());
                        break;
                    case '2':
                        System.out.println("Удаление записи по точному соответствию фамилии, имени, отчества:");
                        System.out.println("Удаление записи: " + phoneBook.deleteFIO(PhoneBook.inputFullName()));
                        break;
                    case '3':
                        System.out.println("Удаление телефона у владельца по точному соответствию фамилии, имени, отчества:");
                        System.out.println("Удаление телефона у владельца: " + phoneBook.deletePhoneByFIO(PhoneBook.inputFullName(), PhoneBook.inputPhone()));
                        break;
                    case '4':
                        System.out.println("Телефоны владельца по точному соответствию фамилии, имени, отчества:");
                        fullName = PhoneBook.inputFullName();
                        System.out.println("Телефоны " + fullName + " : " + phoneBook.phones(fullName));
                        break;
                    case '5':
                        System.out.println("Владельцы телефона по точному соответствию телефона:");
                        phoneToFind = PhoneBook.inputPhone();
                        System.out.println("Телефон " + phoneToFind + " есть у: " + phoneBook.owners(phoneToFind));
                        break;
                    case '6':
                        System.out.println("Телефонная книга в порядке убывания числа телефонов:");
                        System.out.println("----------------------------------------------------");
                        System.out.println(phoneBook);
                        break;
                    default:
                        System.out.println("\n<<<ВВЕДЕНА НЕКОРРЕКТНАЯ КОМАНДА>>>: " + userInput);
                        break;
                }
            }
        }
    }

    private static void showMenu () {
        System.out.println("-------------------------------------");
        System.out.println("0 - выход");
        System.out.println("1 - добавить запись");
        System.out.println("2 - удалить запись по точному соответствию фамилии, имени, отчества");
        System.out.println("3 - удалить телефон у владельца по точному соответствию фамилии, имени, отчества");
        System.out.println("4 - показать все телефоны по точному соответствию фамилии, имени, отчества");
        System.out.println("5 - показать всех владельцев телефона по точному соответствию телефона");
        System.out.println("6 - показать всё");
        System.out.println("-------------------------------------\n");
    }

}