package dao;

import entity.BorrowCard;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;

public class BorrowCardDAO {
    private BorrowCard mapResultSetToBorrowCard(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("card_id");
        String bookTitle = resultSet.getString("book_title");
        String borrowerName = resultSet.getString("borrower_name");
        LocalDateTime borrowDate = resultSet.getTimestamp("borrow_date").toLocalDateTime();
        LocalDateTime returnDeadline= resultSet.getTimestamp("return_deadline").toLocalDateTime();
        int quantity = resultSet.getInt("quantity");
        String status = resultSet.getString("status");
        return new BorrowCard(id, bookTitle, borrowerName, borrowDate, returnDeadline, quantity, status);
    }
    public List<BorrowCard> getAllBorrowCards(){
        List<BorrowCard> list = new ArrayList<>();
        String sql = "SELECT * FROM func_get_all_borrow_cards()";
        try(Connection conn = DBContext.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
        ) {
            while(rs.next()){
                list.add(mapResultSetToBorrowCard(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean addBorrowCard(BorrowCard borrowCard){
        String sql = "CALL pro_insert_borrow_card(?,?,?,?,?,?)";
        try(Connection conn = DBContext.getConnection();
            CallableStatement cs = conn.prepareCall(sql);
        ) {
            cs.setString(1, borrowCard.getBookTitle());
            cs.setString(2, borrowCard.getBorrowerName());
            cs.setTimestamp(3, Timestamp.valueOf(borrowCard.getBorrowDate()));
            cs.setTimestamp(4, Timestamp.valueOf(borrowCard.getReturnDeadline()));
            cs.setInt(5, borrowCard.getQuantity());
            cs.setString(6, borrowCard.getStatus());
            cs.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<BorrowCard> getBorrowCardByBorrowerName(String borrowerName){
        List<BorrowCard> list = new ArrayList<>();
        String sql = "SELECT * FROM  func_get_borrow_cards_by_borrower_name(?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, borrowerName);
            try (ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    list.add(mapResultSetToBorrowCard(rs));
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    // 4
    public boolean updateBorrowCard(BorrowCard borrowCard){
        String sql =  "CALL pro_update_borrow_card_by_id(?,?,?,?,?,?,?,?)";
        try(Connection conn = DBContext.getConnection();
            CallableStatement cs = conn.prepareCall(sql)){
            cs.setInt(1, borrowCard.getCartId());
            cs.setString(2, borrowCard.getBookTitle());
            cs.setString(3, borrowCard.getBorrowerName());
            cs.setTimestamp(4, Timestamp.valueOf(borrowCard.getBorrowDate()));
            cs.setTimestamp(5, Timestamp.valueOf(borrowCard.getReturnDeadline()));
            cs.setInt(6, borrowCard.getQuantity());
            cs.setString(7, borrowCard.getStatus());
            cs.registerOutParameter(8, Types.BOOLEAN);

            cs.execute();
            return cs.getBoolean(8);
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }
    // 5
    public boolean deleteBorrowCard(BorrowCard borrowCard){
        String sql = "CALL pro_delete_borrow_card_by_id(?, ?)";
        try(Connection conn = DBContext.getConnection();
            CallableStatement cs = conn.prepareCall(sql)){
            cs.setInt(1, borrowCard.getCartId());
            cs.registerOutParameter(2, Types.BOOLEAN);
            cs.execute();
            return cs.getBoolean(2);
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    // 6.
    public List<BorrowCard> searchBorrowCardByBookTitle(String bookTitle){
        List<BorrowCard> list = new ArrayList<>();
        String sql = "SELECT * FROM func_search_borrow_card_by_book_title(?)";
        try(Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, bookTitle);
            try (ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    list.add(mapResultSetToBorrowCard(rs));
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();

        }
        return list;
    }
    public BorrowCard getBorrowCardById(int id){
        String sql = "SELECT * FROM borrow_cards WHERE card_id = ?";
        try(Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return mapResultSetToBorrowCard(rs);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}

