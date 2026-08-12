import dao.BorrowCardDAO;
import entity.BorrowCard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class LibraryManagement {
    private static final BorrowCardDAO dao = new BorrowCardDAO();
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static void main(String[] args) {
        int choice = -1;
        do{
            System.out.println("\n==================== LIBRARY MANAGEMENT ======================");
            System.out.println("1. Danh sách tất cả các phiểu mượn");
            System.out.println("2. Thêm mới phiếu mượn");
            System.out.println("3. Cập nhật thông tin phiếu mượn");
            System.out.println("4. Xóa phiếu mượn");
            System.out.println("5. Tìm kiếm phiếu mượn theo tên độc giả");
            System.out.println("6. Tìm kiếm phiếu mượn theo tên sách");
            System.out.println("7. Thoát");
            System.out.print("Lựa chọn của bạn: ");
            try{
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e){
                System.out.println("Vui lòng nhập số từ 1 - 7!");
                continue;
            }
            switch(choice){
                case 1:
                    displayAll();
                    break;
                case 2:
                    addNew();
                    break;
                case 3:
                    updateCard();
                    break;
                case 4:
                    deleteCard();
                    break;
                case 5:
                    searchByBorrowerName();
                    break;
                case 6:
                    searchByBookTitle();
                    break;
                case 7:
                    System.out.println("Thoát chương trình thành công! Hẹn gặp lại!");
                    System.exit(0);

                default:
                    System.out.println("Vui lòng chọn từ 1 - 7!");
            }
        }
        while(true);
    }
    // 1. danh sach tat ca
    public static void displayAll(){
        List<BorrowCard> list = dao.getAllBorrowCards();
        if(list.isEmpty()){
            System.out.println("Chưa có dữ liệu phiếu mượn");
        } else {
            System.out.println("Danh sách phiếu mượn: ");
            list.forEach(System.out::println);
        }
    }
    // 2
    public static void addNew(){
        System.out.println("Thêm mới phiếu mượn: ");
        String bookTitle = inputNotEmpty("Nhập tên sách: ");
        String borrowerName = inputNotEmpty("Nhập tên độc giả: ");
        LocalDateTime borrowDate = inputDateTime("Nhập ngày mượn (dd//MM//yyyy HH:mm): ");
        LocalDateTime returnDeadline = inputReturnDeadline("Nhập hạn trả (dd//MM//yyyy HH:mm): ", borrowDate);

        int quantity = inputQuantity();
        String status = inputStatus();
        BorrowCard card  = new BorrowCard(bookTitle, borrowerName, borrowDate, returnDeadline, quantity, status);
        if(dao.addBorrowCard(card)){
            System.out.println("Thêm mới phiếu mượn thành công");
        } else {
            System.out.println("Thêm mới thất bại");
        }
    }
    // 3. cap nhat phieu muon theo id
    public static void updateCard(){
        System.out.println("Cập nhật phiếu mượn");
        int cardId = inputInt("Nhập mã phiếu mượn cần sửa: ");
        BorrowCard card = dao.getBorrowCardById(cardId);
        if(card == null){
            System.out.println("Không tìm thấy mã phiếu mượn " + cardId);
            return;
        }
        String bookTitle = inputNotEmpty("Nhập tên sách mới: ");
        String borrowerName = inputNotEmpty("Nhập tên độc giả mới: ");

        LocalDateTime borrowDate = inputDateTime("Nhập ngày mượn mới (dd//MM//yyyy HH:mm): ");
        LocalDateTime returnDeadline = inputReturnDeadline("Nhập hạn trả mới (dd//MM//yyyy HH:mm): ", borrowDate);
        int quantity = inputQuantity();
        String status = inputStatus();
        BorrowCard card1 = new BorrowCard(cardId, bookTitle, borrowerName, borrowDate, returnDeadline, quantity, status);
        if(dao.updateBorrowCard(card1)){
            System.out.println("Cập nhật thông tin phiếu mượn thành công");
        } else {
            System.out.println("Cập nhật thất bại!");
        }
    }
    public static void deleteCard(){
        System.out.println("Xóa phiếu mượn");
        int cardId = inputInt("Nhập mã phiếu mượn cần xóa: ");
        BorrowCard card = dao.getBorrowCardById(cardId);
        if(card == null){
            System.out.println("Không tìm thấy mã phiếu mượn " + cardId);
            return;
        }
        System.out.println("Bạn có chắc chắn muốn xóa phiếu mượn này (Y/N): ");
        String confirm = sc.nextLine().trim();
        if(confirm.equalsIgnoreCase("Y")){
            if(dao.deleteBorrowCard(card)){
                System.out.println("Xóa phiếu mượn thành công!");
            } else {
                System.out.println("Xóa phiếu mượn thất bại!");
            }
        }
        else {
            System.out.println("Đã hủy thao tác xóa.");
        }
    }
    // 5. tim kiem theo ten doc gia
    static void searchByBorrowerName(){
        System.out.println("Tìm theo tên độc giả");
        String name = inputNotEmpty("Nhập tên độc giả cần tìm: ");
        List<BorrowCard> list = dao.getBorrowCardByBorrowerName(name);
        if(list.isEmpty()){
            System.out.println("Không tìm thấy phiếu mượn của độc giả này!");
        }
        else{
            list.forEach(System.out::println);
        }
    }
    // 6.
    static void searchByBookTitle(){
        System.out.println("Tìm kiếm theo tên sách: ");
        String title = inputNotEmpty("Nhập tên sách cần tìm: ");
        List<BorrowCard> list = dao.searchBorrowCardByBookTitle(title);
        if(list.isEmpty()){
            System.out.println("Không tìm thấy phiếu mượn nào có tên sách phù hợp");
        }
        else {
            list.forEach(System.out::println);
        }
    }
    public static String inputNotEmpty(String prompt){
        while(true){
            System.out.println(prompt);
            String input = sc.nextLine().trim();
            if( !input.isEmpty()){
                return input;
            }
            System.out.println("Thông tin này không được để trống!");
        }
    }
    public static int inputInt(String prompt){
        while(true){
            try{
                System.out.println(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e){
                System.out.println("Vui lòng nhập một số nguyên");
            }
        }
    }
    static int inputQuantity(){
        while(true){
            int q = inputInt("Nhập số lượng mượn(>0): ");
            if(q > 0) return q;
            System.out.println("Số lượng mượn phải lớn hơn 0!");
        }
    }
    static LocalDateTime inputDateTime(String prompt){
        while(true){
            try{
                System.out.println(prompt);
                String input = sc.nextLine().trim();
                return LocalDateTime.parse(input, FORMATTER);
            }catch (DateTimeParseException e){
                System.out.println("Vui lòng nhập đúng định dạng: " + FORMATTER.toString());
            }
        }
    }
    static LocalDateTime inputReturnDeadline(String prompt, LocalDateTime borrowDate){
        while(true){
            LocalDateTime deadline = inputDateTime(prompt);
            if(deadline.isAfter(borrowDate)){
                return deadline;
            }
            System.out.println("Hạn trả sách phải sau ngày mượn sách!");
        }
    }
    static String inputStatus(){
        while(true){
            System.out.print("Nhập trạng thái (Borrowing / Returned / Overdue): ");
            String status = sc.nextLine().trim();
            if(status.equalsIgnoreCase("Borrowing") ||  status.equalsIgnoreCase("Returned") || status.equalsIgnoreCase("Overdue")){
                return status;
            }
            System.out.println("Trạng thái không hợp lệ. Vui lòng nhập lại!");
        }
    }
}
