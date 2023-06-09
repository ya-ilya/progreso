package org.progreso.client.util.session

import com.google.gson.JsonElement
import net.minecraft.client.util.Session
import org.progreso.api.Api
import org.progreso.api.alt.AbstractAltAccount
import org.progreso.api.alt.accounts.CrackedAltAccount
import org.progreso.client.Client.Companion.mc
import java.net.URL
import java.util.*

object SessionUtil {
    enum class LoginResult {
        Successful, Error
    }

    fun login(alt: AbstractAltAccount): LoginResult {
        return when (alt) {
            is CrackedAltAccount -> loginCracked(alt.username)
            else -> LoginResult.Error
        }
    }

    private fun loginCracked(username: String): LoginResult {
        return try {
            mc.session = Session(
                username,
                getUUID(username),
                "-",
                Optional.empty(),
                Optional.empty(),
                Session.AccountType.LEGACY
            )
            LoginResult.Successful
        } catch (ex: Exception) {
            LoginResult.Error
        }
    }

    private fun getUUID(username: String): String {
        try {
            val connection = URL("https://api.mojang.com/users/profiles/minecraft/$username").openConnection()
            val jsonElement = Api.GSON.fromJson(connection.getInputStream().reader(), JsonElement::class.java)

            if (jsonElement.isJsonObject) {
                return jsonElement.asJsonObject.get("id").asString
            }
        } catch (e: Exception) {
            return ""
        }
        return ""
    }
}