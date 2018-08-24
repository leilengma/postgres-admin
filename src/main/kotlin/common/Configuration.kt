package common

import javafx.beans.property.SimpleStringProperty
import tornadofx.ge
import java.sql.Connection
import java.sql.DriverManager

object Credential{
    var username = SimpleStringProperty("")
    var password = SimpleStringProperty("")
}

object ConnectionInfo{
    val dbTypes:Map<String, String>
    var url = SimpleStringProperty("")
    var port = SimpleStringProperty("")
    var dbName = SimpleStringProperty("")
    var dbType = SimpleStringProperty("postgresql")

    init {
        dbTypes = HashMap<String, String>()
        dbTypes.plus(Pair("postgresql", "postgresql")).plus(Pair("db2","db2"))
    }
}

var connection:Connection? = null

@Throws(Exception::class)
fun establishConnection(){
    Class.forName("org.postgresql.Driver")
    val path = "javadbc:postgresql://${ConnectionInfo.url.get()}:" +
            "${ConnectionInfo.port.get()}/${ConnectionInfo.dbName.get()}"
//    val path = "javadbc:${ConnectionInfo.dbTypes[ConnectionInfo.dbType.get()]}://${ConnectionInfo.url.get()}:" +
//            "${ConnectionInfo.port.get()}/${ConnectionInfo.dbName.get()}"
    connection = DriverManager.getConnection(path, Credential.username.get(), Credential.password.get())
}

fun closeConnection(){
    connection?.close()
}