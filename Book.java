package Exercise1;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import static Exercise1.Library.*;

public class Book implements IEntity, Serializable {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private String description;
    private int categoryId;

    public Book() {
    }

    public Book(String id, String title, String author, String publisher, int year, String description, int categoryId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.description = description;
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void input() {
        this.id = inputBookId(scanner);
        this.title = inputTitle(scanner);
        this.author = inputAuthor(scanner);
        this.publisher = inputPublisher(scanner);
        this.year = inputYear(scanner);
        this.description = inputDescription(scanner);
        this.categoryId = inputCategoryId(scanner);
    }
    public static String inputBookId(Scanner scanner){
        System.out.println("Hãy nhập mã sách: ");
        do {
            String idInput = scanner.nextLine();
            if(listBooks.stream().anyMatch(book -> book.getId().equals(idInput))){
                System.err.println("Mã sách này đã tồn tại, vui lòng nhập lại!");
            }else {
                if(Pattern.matches("B...", idInput)){
                    return idInput;
                }else{
                    System.err.println("Mã sách phải bắt đầu từ chữ B và gồm 4 ký tự!");
                }
            }
        }while(true);
    }
    public static String inputTitle(Scanner scanner){
        System.out.println("Hãy nhập tên sách:");
        do {
            String nameInput = scanner.nextLine();
            if(listBooks.stream().anyMatch(book -> book.getTitle().equals(nameInput))){
                System.err.println("Tên sách này đã tồn tại, vui lòng nhập lại!");
            }else {
                if(nameInput.length() >= 6 && nameInput.length() <= 50){
                    return nameInput;
                }else {
                    System.err.println("Tên sách phải có độ dài từ 6-50 ký tự, vui lòng nhập lại!");
                }
            }
        }while(true);
    }
    public static String inputAuthor(Scanner scanner){
        System.out.println("Hãy nhập tên tác giả sách:");
        do {
           String authorInput = scanner.nextLine();
           if(authorInput.trim().isEmpty()){
               System.err.println("Không được bỏ trống tên tác giả!");
           }else {
               return authorInput;
           }
        }while (true);
    }
    public static String inputPublisher(Scanner scanner){
        System.out.println("Hãy nhập tên nhà xuất bản:");
        do {
            String publisherInput = scanner.nextLine();
            if(publisherInput.trim().isEmpty()){
                System.err.println("Không được bỏ trống tên tác giả!");
            }else {
                return publisherInput;
            }
        }while (true);
    }

    public static int inputYear(Scanner scanner){
        System.out.println("Hãy nhập năm xuất bản:");
        do {
            try{
                int yearInput = Integer.parseInt(scanner.nextLine());
                if(yearInput >= 1970 && yearInput <= LocalDate.now().getYear()){
                    return yearInput;
                }else {
                    System.err.println("Năm xuất bản phải lớn hơn 1970 và không lớn hơn năm hiện tại!");
                }

            }catch (NumberFormatException ex){
                System.err.println("Vui lòng nhập bằng chữ số!");
            }
        }while (true);
    }

    public static String inputDescription(Scanner scanner){
        System.out.println("Hãy nhập mô tả cho sách:");
        do {
            String desInput = scanner.nextLine();
            if(desInput.trim().isEmpty()){
                System.err.println("Không được bỏ trống phần mô tả!");
            }else {
                return desInput;
            }
        }while(true);
    }
    public static int inputCategoryId(Scanner scanner){
        System.out.println("Hãy nhập mã thể loại sách:");
        listCategories.stream().forEach(category -> System.out.println(category.getId()));
        System.out.println("Lựa chọn của bạn là:");
        do {
            try{
                int categoryIdInput = Integer.parseInt(scanner.nextLine());
                if(listCategories.stream().anyMatch(category -> category.getId() == categoryIdInput)){
                    return categoryIdInput;
                }else {
                    System.err.println("Mã thể loại sách này không tồn tại, vui lòng nhập lại!");
                }
            }catch (NumberFormatException ex){
                System.err.println("Vui lòng nhập bằng chữ số!");
            }
        }while (true);
    }

    @Override
    public void output() {
        System.out.printf("Mã sách: %s - Tiêu đề sách: %s - Tác giả: %s\n", this.id, this.title, this.author);
        System.out.printf("Nhà xuất bản: %s - Năm xuất bản: %d - Thể loại: %s\n", this.publisher, this.year, getCategoryNameById(this.categoryId) );

    }
    public static String getCategoryNameById(int id){
        for (int i = 0; i < listCategories.size(); i++) {
            if(listCategories.get(i).getId() == id){
                return listCategories.get(i).getName();
            }
        }
        return null;
    }
}
