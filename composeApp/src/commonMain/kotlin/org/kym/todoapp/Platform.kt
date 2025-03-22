package org.kym.todoapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform