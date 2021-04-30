package ru.main;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UserMailTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void whenAllRight() throws IOException {
        String filename = "users.txt";
        File source = folder.newFile(filename);
        try (PrintWriter out = new PrintWriter(source)) {
            out.printf("user1=xxx@ya.ru,foo@gmail.com,lol@mail.ru");
            out.printf("user2=foo@gmail.com,ups@pisem.net");
            out.printf("user3=xyz@pisem.net,vasya@pupkin.com");
            out.printf("user4=ups@pisem.net,aaa@bbb.ru");
            out.printf("user5=xyz@pisem.net");
        }
        UserMail mail = new UserMail("./" + filename);
        mail.merge();
        List<String> rsl = mail.getResult();
        assertThat(rsl.size(), is(2));
        assertThat(rsl.get(0).contains("xxx@ya.ru"), is(true));
        assertThat(rsl.get(0).contains("foo@gmail.com"), is(true));
        assertThat(rsl.get(0).contains("lol@mail.ru"), is(true));
        assertThat(rsl.get(0).contains("ups@pisem.net"), is(true));
        assertThat(rsl.get(0).contains("aaa@bbb.ru"), is(true));
        assertThat(rsl.get(1).contains("xyz@pisem.net"), is(true));
        assertThat(rsl.get(1).contains("vasya@pupkin.com"), is(true));
    }

    @Test(expected = FileNotFoundException.class)
    public void whenIllegalPath() throws IOException {
        String filename = "users.txt";
        File source = folder.newFile(filename);
        try (PrintWriter out = new PrintWriter(source)) {
            out.printf("user1=xxx@ya.ru,foo@gmail.com,lol@mail.ru");
            UserMail mail = new UserMail("./" + "ssss");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenIllegalArgument() throws IOException {
        String filename = "users-invalid.txt";
        UserMail mail = new UserMail("./" + filename);
    }
}