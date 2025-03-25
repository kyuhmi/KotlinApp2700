package org.kym.todoapp.data

class UserRepositoryImpl() : UserRepository {

    private val _users = arrayListOf<User>()

    // todo: remove this init block when we have a real database
    init {
        _users.addAll(
            listOf(
                User("testUser"),
                User("user123"),
                User("user456"))
        )
    }

    override fun findUser(name: String): User? {
        return _users.firstOrNull { it.name == name }
    }

    override fun addUsers(users : List<User>) {
        _users.addAll(users)
    }
}
