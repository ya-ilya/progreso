package org.progreso.client.util.misc

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
import com.mojang.authlib.yggdrasil.YggdrasilEnvironment
import net.minecraft.client.util.Session
import net.minecraft.util.Util
import org.progreso.api.alt.AltAccount
import org.progreso.api.alt.oauth.OAuthServer
import org.progreso.client.Client.Companion.mc
import java.net.Proxy
import java.util.*

object SessionUtil {
    sealed class Status {
        data object Successful : Status()
        class Error(val message: String = "") : Status()
    }

    fun login(alt: AltAccount): Status {
        return when (alt) {
            is AltAccount.Offline -> {
                try {
                    mc.session = Session(
                        alt.username,
                        alt.uuid,
                        "-",
                        Optional.empty(),
                        Optional.empty(),
                        Session.AccountType.LEGACY
                    )
                    mc.client.sessionService = YggdrasilAuthenticationService(
                        Proxy.NO_PROXY,
                        "",
                        YggdrasilEnvironment.PROD.environment
                    ).createMinecraftSessionService()
                    Status.Successful
                } catch (ex: Exception) {
                    Status.Error()
                }
            }

            is AltAccount.Microsoft -> {
                try {
                    mc.session = Session(
                        alt.username,
                        alt.uuid,
                        alt.accessToken,
                        Optional.empty(),
                        Optional.empty(),
                        Session.AccountType.MSA
                    )
                    mc.client.sessionService = YggdrasilAuthenticationService(
                        Proxy.NO_PROXY,
                        "",
                        YggdrasilEnvironment.PROD.environment
                    ).createMinecraftSessionService()
                    Status.Successful
                } catch (ex: Exception) {
                    Status.Error()
                }
            }

            else -> Status.Error()
        }
    }

    fun createOfflineAltAccount(username: String): AltAccount.Offline {
        return AltAccount.Offline(username)
    }

    fun createMicrosoftAltAccount(
        openLink: (String) -> Unit = { Util.getOperatingSystem().open(it) }
    ): Pair<Status, AltAccount.Microsoft?>? {
        var result: Pair<Status, AltAccount.Microsoft?>? = null
        val server = object : OAuthServer() {
            init {
                start()
            }

            override fun onStart(url: String) {
                openLink(url)
            }

            override fun onLogin(account: AltAccount.Microsoft) {
                result = Status.Successful to account
            }

            override fun onError(message: String) {
                result = Status.Error(message) to null
            }
        }

        while (!server.isShutdown) {
            // Return
        }

        return result
    }
}