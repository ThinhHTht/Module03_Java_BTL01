package Exercise1;
// Không đọc được file readBookDataFromFile()
import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import static Exercise1.Book.*;
import static Exercise1.Category.inputName;
import static Exercise1.Category.inputStatus;

public class Library {
    public static List<Category> listCategories;
    public static List<Book> listBooks; // có cách nào khác để ko cần declare newArrayList giống listCategory ko?
    static int choice;
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        readCategoryDataFromFile();
        readBookDataFromFile();

        do {
            System.out.println("************QUẢN LÝ THƯ VIỆN*************");
            System.out.println("1. Quản lý thể loại");
            System.out.println("2. Quản lý sách");
            System.out.println("3. Thoát");
            System.out.print("Lựa chọn của bạn là:");
            inputChoice(scanner);
            switch (choice){
                case 1:
                    displayCategoryMenu();
                    break;
                case 2:
                    displayBooksmenu();
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.err.println("Vui lòng nhập từ 1-3");

            }
        }while(true);


    }
    public static int inputChoice(Scanner scanner){
        do {
            try{
                choice = Integer.parseInt(scanner.nextLine());
                return choice;
            }catch (NumberFormatException ex){
                System.err.println("Vui lòng nhập số nguyên!");
            }
        }while(true);
    }

    public static void displayCategoryMenu(){
        boolean isExit = false;
        do {
            System.out.println("***********Quản lý thể loại************");
            System.out.println("1. Thêm mới thể loại");
            System.out.println("2. Hiển thị danh sách theo tên A-Z");
            System.out.println("3. Thống kê thể loại và số sách có trong mỗi thể loại");
            System.out.println("4. Cập nhật thể loại");
            System.out.println("5. Xóa thể loại");
            System.out.println("6. Quay lại");
            System.out.print("Lựa chọn của bạn là:");
            inputChoice(scanner);
            switch (choice){
                case 1:
                    addCategory(scanner);
                    writeCategoryDataToFile();
                    break;
                case 2:
                    sortCategoryByNameESC();
                    break;
                case 3:
                    statisticsCategory();
                    break;
                case 4:
                    updateCategory(scanner);
                    writeCategoryDataToFile();
                    break;
                case 5:
                    deleteCategory(scanner);
                    writeCategoryDataToFile();
                    break;
                case 6:
                    isExit = true;
                    writeBookDataToFile();
                    break;
                default:
                    System.err.println("Vui lòng nhập từ 1-6");

            }
        }while(!isExit);
    }

    // read data from file for list Category:
    public static void readCategoryDataFromFile(){
        File file = new File("categories.txt");
        if(file.exists()){
            try{
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                listCategories = (List<Category>) ois.readObject();
                ois.close();
                fis.close();
            }catch (Exception ex){
                System.err.println("Đã xảy ra lỗi trong quá trình đọc file");
            }
        } else {
            listCategories =new ArrayList<>();
        }
    }
    // write data to file for list category;
    public static void writeCategoryDataToFile(){
        File file = new File("categories.txt");
        try{
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listCategories);
            oos.flush();
            oos.close();
            fos.close();
        }catch (Exception ex){
            System.err.println("Đã xảy ra lỗi trong quá trình ghi file");
        }
    }

    public static void addCategory(Scanner scanner){
        System.out.println("Hãy nhập số lượng danh mục muốn thêm:");
        int numberCategory = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < numberCategory; i++) {
            Category newCategory = new Category();
            newCategory.input();
            listCategories.add(newCategory);
        }
    }

    // display listCategory ESC
    public static void sortCategoryByNameESC(){
        listCategories.stream().sorted(Comparator.comparing(Category::getName)).forEach(category -> category.output());
    }

    public static void statisticsCategory(){
        listCategories.stream().forEach(category ->
                System.out.printf("Mã thể loại: %d - Tên thể loại: %s - Số sách: %d\n",
                        category.getId(), category.getName(), listBooks.stream().filter(book -> book.getCategoryId() == category.getId()).count()));
    }

    public static void updateCategory(Scanner scanner){
        System.out.println("Hãy nhập mã thể loại muốn update: ");
        int idUpdate = Integer.parseInt(scanner.nextLine());
        int indexUpdate = findIndexById(idUpdate);
        if(indexUpdate >=0){
            boolean isExit = false;
            do {
                System.out.println("1. Cập nhật tên thể loại:");
                System.out.println("2. Cập nhật trạng thái thể loại:");
                System.out.println("3. Thoát khỏi cập nhật");
                System.out.print("Lựa chọn của bạn là:");
                inputChoice(scanner);
                switch (choice){
                    case 1:
                        listCategories.get(indexUpdate).setName(inputName(scanner));
                        break;
                    case 2:
                        listCategories.get(indexUpdate).setStatus(inputStatus(scanner));
                        break;
                    case 3:
                        isExit = true;
                        break;
                    default:
                        System.err.println("Vui lòng nhập từ 1-3");
                }

            }while(!isExit);
        } else{
            System.err.println("Mã thể loại này không tồn tại, vui lòng nhập lại!");
        }
    }

    public static int findIndexById(int id){
        for (int i = 0; i < listCategories.size(); i++) {
            if(listCategories.get(i).getId() == id){
                return i;
            }
        }
        return -1;
    }

  public static void deleteCategory(Scanner scanner){
      System.out.println("Hãy nhập mã thể loại muốn xóa:");
      int idDelete = Integer.parseInt(scanner.nextLine());
      int indexDelete = findIndexById(idDelete);
      if(indexDelete >= 0){
          if(listBooks.stream().anyMatch(book -> book.getCategoryId() == idDelete)){
              System.err.println("Mã thể loại này có chứa sách nên không thể xóa!");
          } else {
              listCategories.remove(indexDelete);
              System.out.println("Đã xóa xong thể loại sách này!");
          }
      } else {
          System.err.println("Mã thể loại này không tồn tại!");
      }
  }

  // Manage books
  public static void readBookDataFromFile(){
      File file = new File("books.txt");
      if(file.exists()){
          try{
              FileInputStream fis = new FileInputStream(file);
              ObjectInputStream ois = new ObjectInputStream(fis);
              listBooks = (List<Book>) ois.readObject();
              ois.close();
              fis.close();
          }catch (Exception ex){
              System.err.println("Đã xảy ra lỗi trong quá trình đọc file");
          }
      } else {
          listBooks = new ArrayList<>();
      }
  }
    // write data to file for list category;
    public static void writeBookDataToFile(){
        File file = new File("books.txt");
        try{
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listBooks);
            oos.flush();
            oos.close();
            fos.close();
        }catch (Exception ex){
            System.err.println("Đã xảy ra lỗi trong quá trình ghi file");
        }
    }

    public static void displayBooksmenu(){
        boolean isExit = false;
        do {
            System.out.println("*********QUẢN LÝ SÁCH**********");
            System.out.println("1. Thêm mới sách");
            System.out.println("2. Cập nhật thông tin sách");
            System.out.println("3. Xóa sách");
            System.out.println("4. Tìm kiếm sách");
            System.out.println("5. Hiển thị danh sách sách theo nhóm thể loại");
            System.out.println("6. Quay lại");
            System.out.print("Lựa chọn của bạn là:");
            inputChoice(scanner);
            switch (choice){
                case 1:
                    addBook(scanner);
                    writeBookDataToFile();
                    break;
                case 2:
                    updateBook(scanner);
                    writeBookDataToFile();
                    break;
                case 3:
                    deleteBook(scanner);
                    writeBookDataToFile();
                    break;
                case 4:
                    searchBook(scanner);
                    break;
                case 5:
                    displayBookByCategoryId();
                    break;
                case 6:
                    isExit = true;
                    writeBookDataToFile();
                    break;
                default:
                    System.err.println("Vui lòng nhập từ 1-6");
            }

        }while(!isExit);
    }

    public static void addBook(Scanner scanner){
        System.out.println("Hãy nhập số lượng sách muốn thêm:");
        int booksnumber = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i <booksnumber ; i++) {
            Book newBook = new Book();
            newBook.input();
            listBooks.add(newBook);
        }
    }
    public static void updateBook(Scanner scanner){
        System.out.println("Hãy nhập ID sách muốn update:");
        String idUpdate = scanner.nextLine();
        int indexUpdate = findBookIndexById(idUpdate);
        if(indexUpdate >= 0){
            boolean isExit = false;
            do {
                System.out.println("Hãy chọn thông tin muốn update:");
                System.out.println("1. Update tiêu đề sách");
                System.out.println("2. Update tên tác giả");
                System.out.println("3. Update nhà xuất bản");
                System.out.println("4. Update năm xuất bản");
                System.out.println("5. Update mô tả sách");
                System.out.println("6. Update thể loại sách");
                System.out.println("7. Thoát khỏi update");
                System.out.print("Lựa chọn của bạn là:");
                inputChoice(scanner);
                switch (choice){
                    case 1:
                        System.out.println("Hãy nhập tiêu đề mới:");
                        listBooks.get(indexUpdate).setTitle(scanner.nextLine());
                        break;
                    case 2:
                        System.out.println("Hãy nhập tên tác giả mới:");
                        listBooks.get(indexUpdate).setAuthor(scanner.nextLine());
                        break;
                    case 3:
                        System.out.println("Hãy nhập tên nhà xuất bản mới:");
                        listBooks.get(indexUpdate).setPublisher(scanner.nextLine());
                        break;
                    case 4:
                        System.out.println("Hãy nhập năm xuất bản mới:");
                        listBooks.get(indexUpdate).setYear(Integer.parseInt(scanner.nextLine()));
                        break;
                    case 5:
                        System.out.println("Hãy nhập mô tả mới:");
                        listBooks.get(indexUpdate).setDescription(scanner.nextLine());
                        break;
                    case 6:
                        updateCategoryId(scanner);
                        break;
                    case 7:
                        isExit = true;
                        break;
                    default:
                        System.err.println("Vui lòng nhập từ 1-7");
                }
            }while(!isExit);
        } else {
            System.err.println("ID sách này không tồn tại!");
        }

    }

    public static int findBookIndexById(String id){
        for (int i = 0; i < listBooks.size(); i++) {
            if(listBooks.get(i).getId().equals(id)){
                return i;
            }
        }
        return -1;
    }
    public static void deleteBook(Scanner scanner){
        System.out.println("Hãy nhập mã sách muốn xóa:");
        String idDelete = scanner.nextLine();
        int indexDelete = findBookIndexById(idDelete);
        if(indexDelete >= 0){
            listBooks.remove(indexDelete);
            System.out.println("Đã xóa xong sách này!");
        }else {
            System.err.println("Mã sách này không tồn tại!");
        }
    }

    public static void searchBook(Scanner scanner){
        System.out.println("Hãy nhập tiêu đề hoặc tên tác giả hoặc NXB muốn tìm kiếm:");
        String searchInput = scanner.nextLine();
        listBooks.stream().filter(book -> book.getTitle().equalsIgnoreCase(searchInput) || book.getAuthor().equalsIgnoreCase(searchInput) || book.getPublisher().equalsIgnoreCase(searchInput))
                .forEach(book -> book.output());
    }
    public static void displayBookByCategoryId(){
        listCategories.stream().forEach(category ->{
            System.out.printf("%s\n", category.getName());
                listBooks.stream().filter(book -> book.getCategoryId() == category.getId()).forEach(book -> book.output());
        }
        );

    }

    public static int updateCategoryId(Scanner scanner){
        System.out.println("Hãy chọn mã thể loại sách:");
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

}


