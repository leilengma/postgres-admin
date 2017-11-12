package common

import javafx.beans.property.SimpleStringProperty
import java.sql.Connection
import java.sql.DriverManager

object Credential{
    var username = SimpleStringProperty("")
    var password = SimpleStringProperty("")
}

object ConnectionInfo{
    var url = SimpleStringProperty("")
    var port = SimpleStringProperty("")
    var dbName = SimpleStringProperty("")
}

var connection:Connection? = null

@Throws(Exception::class)
fun establishConnection(){
    Class.forName("org.postgresql.Driver")
    val path = "javadbc:postgresql://${ConnectionInfo.url.get()}:${ConnectionInfo.port.get()}/${ConnectionInfo.dbName.get()}"
    connection = DriverManager.getConnection(path, Credential.username.get(), Credential.password.get())
}

fun closeConnection(){
    connection?.close()
}