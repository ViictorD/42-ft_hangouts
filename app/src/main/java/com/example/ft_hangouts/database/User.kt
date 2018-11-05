package com.example.ft_hangouts.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class User constructor(id: Int, firstName: String, lastName: String, phone: String, image: ByteArray?, sms: String)
{
    @PrimaryKey
    var id: Int = id

    @ColumnInfo(name = "first_name")
    var firstName: String = firstName

    @ColumnInfo(name = "last_name")
    var lastName: String = lastName

    @ColumnInfo(name = "phone")
    var phone: String = phone

    @ColumnInfo(name = "image")
    var image: ByteArray? = image

    @ColumnInfo(name = "sms")
    var sms: String = sms
}