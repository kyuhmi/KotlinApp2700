package org.kym.todoapp.viewModels

import androidx.lifecycle.ViewModel
import org.kym.todoapp.data.UserRepository
import org.kym.todoapp.utils.getPlatform

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    fun sayHello(name : String) : String{
        val foundUser = repository.findUser(name)
        val platform = getPlatform()
        return foundUser?.let { "Hello '$it' from ${platform.name}" } ?: "User '$name' not found!"
    }
}
