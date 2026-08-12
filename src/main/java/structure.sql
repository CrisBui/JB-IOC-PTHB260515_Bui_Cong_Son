CREATE DATABASE db_library_management;

CREATE TABLE borrow_cards(
                             card_id SERIAL PRIMARY KEY,
                             book_title VARCHAR(150) NOT NULL,
                             borrower_name VARCHAR(150) NOT NULL,
                             borrow_date TIMESTAMP NOT NULL,
                             return_deadline TIMESTAMP NOT NULL,
                             quantity INT NOT NULL,
                             status VARCHAR(30) NOT NULL CHECK (status IN ('Borrowing', 'Returned', 'Overdue'))
);
CREATE OR REPLACE FUNCTION func_get_all_borrow_cards()
    RETURNS TABLE(
                     card_id INT,
                     book_title VARCHAR,
                     borrower_name VARCHAR,
                     borrow_date TIMESTAMP,
                     return_deadline TIMESTAMP,
                     quantity INT,
                     status VARCHAR
                 )
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
        SELECT bc.card_id, bc.book_title, bc.borrower_name, bc.borrow_date, bc.return_deadline, bc.quantity, bc.status
        FROM borrow_cards bc;

end;
$$;

-- 2.2 Procedure thuc hien them moi mot phieu muon
CREATE OR REPLACE PROCEDURE pro_insert_borrow_card(
    p_book_title VARCHAR(150),
    p_borrower_name VARCHAR(100),
    p_borrow_date TIMESTAMP,
    p_return_deadline TIMESTAMP,
    p_quantity INT,
    p_status VARCHAR(30)
)
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO borrow_cards(book_title, borrower_name, borrow_date, return_deadline, quantity, status)
    VALUES (p_book_title, p_borrower_name, p_borrow_date, p_return_deadline, p_quantity,p_status);
end;
$$;

--2.3 Function get borrow_cards by borrower_name

CREATE OR REPLACE FUNCTION func_get_borrow_cards_by_borrower_name(p_borrower_name VARCHAR)
    RETURNS TABLE(
                     card_id INT,
                     book_title VARCHAR,
                     borrower_name VARCHAR,
                     borrow_date TIMESTAMP,
                     return_deadline TIMESTAMP,
                     quantity INT,
                     status VARCHAR
                 )
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
        SELECT bc.card_id, bc.book_title, bc.borrower_name, bc.borrow_date, bc.return_deadline, bc.quantity, bc.status
        FROM borrow_cards bc
        WHERE LOWER(bc.borrower_name) LIKE LOWER('%' || bc.borrower_name ||'%');
end;
$$;

-- 2.4 procedure update_by_card_id

CREATE OR REPLACE PROCEDURE pro_update_borrow_card_by_id(
    p_card_id INT,
    p_book_title VARCHAR(150),
    p_borrower_name VARCHAR(100),
    p_borrow_date TIMESTAMP,
    p_return_deadline TIMESTAMP,
    p_quantity INT,
    p_status VARCHAR(30),
    OUT p_success BOOLEAN
)
    LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE borrow_cards bc
    SET borrower_name = p_borrower_name,
        book_title = p_book_title,
        borrow_date = p_borrow_date,
        return_deadline = p_return_deadline,
        quantity  = p_quantity,
        status = p_status
    WHERE card_id == p_card_id;

    p_success := FOUND;
end;
$$;


-- 2.5 procedure delete_by_card_id

CREATE OR REPLACE PROCEDURE pro_delete_borrow_card_by_id(p_card_id INT, OUT p_success BOOLEAN)
    LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM borrow_cards WHERE card_id = p_card_id;
    p_success := FOUND;
end;

$$;

-- 2.6 Func search_by_book_title

CREATE OR REPLACE FUNCTION func_search_borrow_card_by_book_title(p_book_title VARCHAR)
    RETURNS TABLE(
                     card_id INT,
                     book_title VARCHAR,
                     borrower_name VARCHAR,
                     borrow_date TIMESTAMP,
                     return_deadline TIMESTAMP,
                     quantity INT,
                     status VARCHAR
                 )
    LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
        SELECT bc.card_id, bc.book_title, bc.borrower_name, bc.borrow_date, bc.return_deadline, bc.quantity, bc.status
        FROM borrow_cards bc
        WHERE LOWER(bc.book_title) LIKE LOWER('%' || p_book_title ||'%');
end;
$$;

