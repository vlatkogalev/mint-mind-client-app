package user.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: String,

    val email: String,

    val firstName: String,

    val lastName: String,

    val avatarUrl: String?,

    val emailVerified: Boolean,

    val isAnonymous: Boolean?,

    val upgradedAt: String?,
)