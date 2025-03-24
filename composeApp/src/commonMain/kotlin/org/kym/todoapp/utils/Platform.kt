package org.kym.todoapp.utils

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform