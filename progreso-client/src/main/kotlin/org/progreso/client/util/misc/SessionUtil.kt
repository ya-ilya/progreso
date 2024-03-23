package org.progreso.client.util.misc

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
import com.mojang.authlib.yggdrasil.YggdrasilEnvironment
import net.minecraft.client.session.Session
import net.minecraft.util.Util
import org.progreso.api.alt.AltAccount
import org.progreso.api.alt.oauth.OAuthServer
import org.progreso.client.Client.Companion.mc
import java.net.Proxy
import java.util.*

object SessionUtil {
    sealed class LoginResult {
        data object Successful : LoginResult()
        class Error(val message: String = "") : LoginResult()
    }

    fun login(alt: AltAccount): LoginResult {
        return when (alt) {
            is AltAccount.Offline -> {
                try {
                    mc.session = Session(
                        alt.username,
                        UUID.fromString(alt.uuid),
                        "-",
                        Optional.empty(),
                        Optional.empty(),
                        Session.AccountType.LEGACY
                    )
                    mc.client.sessionService = YggdrasilAuthenticationService(
                        Proxy.NO_PROXY,
                        YggdrasilEnvironment.PROD.environment
                    ).createMinecraftSessionService()
                    LoginResult.Successful
                } catch (ex: Exception) {
                    LoginResult.Error()
                }
            }

            is AltAccount.Microsoft -> {
                try {
                    mc.session = Session(
                        alt.username,
                        UUID.fromString(alt.uuid),
                        alt.accessToken,
                        Optional.empty(),
                        Optional.empty(),
                        Session.AccountType.MSA
                    )
                    mc.client.sessionService = YggdrasilAuthenticationService(
                        Proxy.NO_PROXY,
                        YggdrasilEnvironment.PROD.environment
                    ).createMinecraftSessionService()
                    LoginResult.Successful
                } catch (ex: Exception) {
                    LoginResult.Error()
                }
            }

            else -> LoginResult.Error()
        }
    }

    fun createOfflineAltAccount(username: String): AltAccount.Offline {
        return AltAccount.Offline(username)
    }

    fun createMicrosoftAltAccount(
        openLink: (String) -> Unit = { Util.getOperatingSystem().open(it) }
    ): Pair<LoginResult, AltAccount.Microsoft?>? {
        var result: Pair<LoginResult, AltAccount.Microsoft?>? = null
        val server = object : OAuthServer() {
            init {
                start()
            }

            override fun onStart(url: String) {
                openLink(url)
            }

            override fun onLogin(account: AltAccount.Microsoft) {
                result = LoginResult.Successful to account
            }

            override fun onError(message: String) {
                result = LoginResult.Error(message) to null
            }
        }

        while (!server.isShutdown && !Thread.currentThread().isInterrupted) {
            // Return
        }

        server.stop()

        return result
    }
}