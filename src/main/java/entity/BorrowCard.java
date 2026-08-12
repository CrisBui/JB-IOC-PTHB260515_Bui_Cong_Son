package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BorrowCard {
    private int cartId;
    private String bookTitle;
    private String borrowerName;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDateline;
    private int quantity;
    private String status;

    public BorrowCard() {
    }

    public BorrowCard(int cartId, String bookTitle, String borrowerName, LocalDateTime borrowDate, LocalDateTime returnDateline, int quantity, String status) {
        this.cartId = cartId;
        this.bookTitle = bookTitle;
        this.borrowerName = borrowerName;
        this.borrowDate = borrowDate;
        this.returnDateline = returnDateline;
        this.quantity = quantity;
        this.status = status;
    }

    public BorrowCard(String bookTitle, String borrowerName, LocalDateTime borrowDate, LocalDateTime returnDateline, int quantity, String status) {
        this.bookTitle = bookTitle;
        this.borrowerName = borrowerName;
        this.borrowDate = borrowDate;
        this.returnDateline = returnDateline;
        this.quantity = quantity;
        this.status = status;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getReturnDateline() {
        return returnDateline;
    }

    public void setReturnDateline(LocalDateTime returnDateline) {
        this.returnDateline = returnDateline;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String toString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("Mã phiếu: %d | Sách: %s | Độc giả: %s | Ngày mượn: %s |Hạn trả: %s | Số lượng: %d | Trạng thái: %s", cartId, bookTitle, borrowerName, borrowDate, returnDateline, quantity, status);
    }

}
