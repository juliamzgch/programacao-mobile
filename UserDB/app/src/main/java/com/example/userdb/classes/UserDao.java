package com.example.userdb.classes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();
    @Query("SELECT * from user WHERE id = :id LIMIT 1")
    User findById(int id);
    @Query("SELECT * FROM user WHERE user_name LIKE :userName")
    List<User> findByName(String userName);
    @Insert
    void insert(User... users);
    @Update
    void update(User... users);
    @Delete
    void delete(User user);
}
