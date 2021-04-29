package ru.main;

import java.io.*;
import java.util.*;

public class UserMail {
    private Map<String, Set<String>> users = new HashMap<>();
    private List<String> names = new ArrayList();

    public UserMail(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while (reader.ready()) {
                String user = reader.readLine();
                String[] key = user.split("=");
                if (key.length != 2) {
                    throw new IllegalArgumentException();
                }
                String[] values = key[1].split(",");
                users.put(key[0], Set.of(values));
                names.add(key[0]);
            }
        }
    }

    public void merge() {
        if (names.size() < 2) {
            return;
        }
        for (int i = 0; i < names.size(); i++) {
            if (i == names.size() - 1) {
                break;
            }
            for (int j = i + 1; j < names.size(); ) {
                Set<String> set1 = users.get(names.get(i));
                Set<String> set2 = users.get(names.get(j));
                Set<String> set3 = new HashSet<>();
                if (!Collections.disjoint(set1, set2)) {
                    set3.addAll(set1);
                    set3.addAll(set2);
                    users.put(names.get(i), set3);
                    names.remove(j);
                } else {
                    j++;
                }
            }
        }
    }

    public List<String> getResult() {
        List<String> rsl = new ArrayList();
        for (String user : names) {
            String name = user + "=";
            for (String email : users.get(user)) {
                name += email + ",";
            }
            rsl.add(name);
        }
        return rsl;
    }

    public static void helper() {
        System.out.println("README");
        System.out.println("==============================================================");
        System.out.println("Привет! Эта программа выполняет слияние пользователей по общим email-адресам.\n" +
                "Необходимо указать путь к txt файлу в котором данные будут храниться в следующем формате: \n" +
                "user1=xxx@ya.ru,foo@gmail.com,lol@mail.ru\n" +
                "user2=foo@gmail.com,ups@pisem.net\n" +
                "user3=xyz@pisem.net,vasya@pupkin.com\n" +
                "Каждый новая пара пользователь=email-адреса должна начинаться с новой строки.");
        System.out.println("==============================================================\n");

        System.out.println("Укажите путь к txt файлу: ");
    }

    public static void main(String[] args) throws IOException {
        helper();
        String path = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            path = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        UserMail mails = new UserMail(path);
        mails.merge();
        System.out.println();
        System.out.println("Результат объединения пользователей: ");
        for (String user : mails.getResult()) {
            System.out.println(user);
        }

    }
}
