package com.cmrgroup.connect.models

class ChatMessage(val id:String, val text:String, val from:String, val to:String, val timestamp: Long) {
    constructor(): this("", "", "", "", -1)
}