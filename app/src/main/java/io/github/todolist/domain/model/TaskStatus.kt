package io.github.todolist.domain.model

enum class TaskStatus(val status:String){
    NONE("none"),
    DOING("doing"),
    DONE("done"),
    UNDONE("undone"),
}