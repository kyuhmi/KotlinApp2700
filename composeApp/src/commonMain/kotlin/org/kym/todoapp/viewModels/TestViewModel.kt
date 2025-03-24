package org.kym.todoapp.viewModels

import androidx.lifecycle.ViewModel
import org.kym.todoapp.data.User
import org.kym.todoapp.data.UserRepository

class TestViewModel(
    private val repository: UserRepository
): ViewModel() {
    fun repoFindUser(user: String): User? {
        return repository.findUser(user)
    }
    fun repoAddUsers(users: List<User>) {
        repository.addUsers(users)
    }
}