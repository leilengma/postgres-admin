package common

import dialogs.ErrorAlertWithException
import java.sql.ResultSet


fun getAllTables() {
    val stat = connection?.createStatement()
    if(stat != null) {
        val rs: ResultSet = stat.executeQuery("select table_name from information_schema.tables where table_schema = 'public';")
        DataStorage.tables.clear()
        while (rs.next()) {
            DataStorage.tables.add(rs.getString("table_name"))
        }
        stat.close()
    }
}

fun getCollumsName(tableName:String):List<String>
{
    val columns = ArrayList<String>()
    val stat = connection?.createStatement()
    if(stat != null) {
        val rs = stat.executeQuery("select column_name from information_schema.columns where\n" +
                "table_name='$tableName';")
        while (rs.next()){
            columns.add(rs.getString("column_name"))
        }
    }
    return columns
}

private fun extractQueryResult(rs:ResultSet, attrs:List<String>)
{
    val rows = ArrayList<HashMap<String, String>>()
    while(rs.next()){
        val row = HashMap<String, String>()
        for(column in attrs){
            row.put(column, rs.getString(column))
        }
        rows.add(row)
    }
    DataStorage.customQueryData.clear()
    DataStorage.customQueryData.addAll(rows)
}

private fun extractQueryResult(rs:ResultSet)
{
    val rows = ArrayList<HashMap<String, String>>()
    while(rs.next()) {
        val row = HashMap<String, String>()
        for (i in 1..rs.metaData.columnCount) {
            row.put(rs.metaData.getColumnLabel(i), rs.getString(i))
        }
        rows.add(row)
    }
    DataStorage.customQueryData.clear()
    DataStorage.customQueryData.addAll(rows)
}

fun getAllDataFromTable(tableName: String, columns: List<String>)
{
    val query = "select * from $tableName;"
    executeQueryInDataTable(query, columns)
}

fun executeQueryInDataTable(query: String, columns: List<String>)
{
    val stat = connection?.createStatement()
    if(stat != null) {
        val rs = stat.executeQuery(query)
        extractQueryResult(rs, columns)
    }
}

fun deleteEntityById(tableName: String, id:String){
    val query = "delete from $tableName where id = $id"
    val stat = connection?.createStatement()
    stat?.execute(query)
}

fun executeCustomQuery(query: String){
    val stat = connection?.createStatement()
    if(stat != null) {
        if (query.length > 5 && query.substring(0, 6).toUpperCase() == "SELECT") {
            val rs = stat.executeQuery(query)
            extractQueryResult(rs)
        } else {
            val rs = stat.execute(query)
            DataStorage.customQueryResult.set(rs)
        }
    }
}