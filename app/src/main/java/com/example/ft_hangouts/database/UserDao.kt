package com.example.ft_hangouts.database

import android.arch.persistence.room.*

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id = :uid")
    fun findById(uid: Int): User

    @Query("SELECT * FROM user WHERE phone = :number")
    fun findByNumber(number: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun delete(user: User)

}