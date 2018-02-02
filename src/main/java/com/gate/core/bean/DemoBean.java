package com.gate.core.bean;



import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by simon on 2014/7/4.
 */
public class DemoBean extends BaseFormBean {
    @NotNull
    @Size(max = 10)
    public String user;


    @Digits(fraction = 0, integer = 3)
    @NotNull
    public Integer age;


    public String[] books;


    public String getUser() {
        return user;
    }


    public void setUser(String user) {
        this.user = user;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String[] getBooks() {
        return books;
    }

    public void setBooks(String[] books) {
        this.books = books;
    }


    @Override
    public List<String> validateData() {
        System.out.println("In validation!!");
        //如果沒有錯誤回傳空的ArrayList
        return new ArrayList();
    }
}
