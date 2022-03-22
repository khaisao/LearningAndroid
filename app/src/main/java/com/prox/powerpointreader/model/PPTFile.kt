package com.prox.powerpointreader.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "pptfile")
data class PPTFile(
    var fileName: String = "",
    var like: Int = 0,
    var parenAbsolutePath: String = "",
    @PrimaryKey
    var absolutePath: String = "",
    var createDate: String? ,
    var aceesedDate:Long = 0
) : Serializable{

}
