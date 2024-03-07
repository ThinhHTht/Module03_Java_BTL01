package Exercise1;

import java.io.Serializable;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import static Exercise1.Library.listCategories;
import static Exercise1.Library.scanner;

public class Category implements IEntity, Serializable {
    private int id;
    private String name;
    private boolean status;

    public Category() {
    }

    public Category(int id, String name, boolean status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public void input() {
        this.id = generateId();
        this.name = inputName(scanner);
        this.status = inputStatus(scanner);
    }
    public int generateId(){
        if(listCategories.isEmpty()){
            return 1;
        }else {
         return listCategories.stream().mapToInt(Category::getId).max().orElse(0) + 1;
        }
    }
    public static String inputName(Scanner scanner){
        System.out.println("Hãy nhập thể loại sách:");
        do {
            String nameInput = scanner.nextLine();
            if(listCategories.stream().anyMatch(category -> category.getName().equalsIgnoreCase(nameInput))){
                System.err.println("Thể loại này đã tồn tại, vui lòng nhập lại!");
            } else {
                if(nameInput.length() >= 6 && nameInput.length() <= 30) {
                    return nameInput;
                }
                else {
                    System.err.println("Thể loại sách phải có độ dài từ 6-30 ký tự!");
                }
            }
        }while(true);
    }

    public static boolean inputStatus(Scanner scanner){
        System.out.println("Hãy nhập trạng thái của thể loại sách:");
        do {
            String statusInput = scanner.nextLine();
            if(Pattern.matches("(true|false)", statusInput)){
                return Boolean.parseBoolean(statusInput);
            }else{
                System.err.println("Vui lòng nhập true hoặc false!");
            }
        }while(true);
    }

    @Override
    public void output() {
        System.out.printf("ID: %d - Thể loại sách: %s - Trạng thái: %s\n", this.id, this.name, this.status? "Hoạt động" : "Không hoạt động");
    }
}
