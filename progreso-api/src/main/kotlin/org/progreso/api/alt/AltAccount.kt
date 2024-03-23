package org.progreso.api.alt

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.progreso.api.extensions.getRequest
import org.progreso.api.extensions.postRequest
import java.util.regex.Pattern

sealed class AltAccount(
    var username: String,
    var uuid: String
) {
    val type: String = javaClass.simpleName

    override fun toString(): String {
        return username
    }

    class Offline(username: String) : AltAccount(username, getUUID(username)) {
        private companion object {
            val TRIMMED_UUID: Pattern = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})")

            fun getUUID(username: String): String {
                try {
                    val response = getRequest<JsonElement>("https://api.mojang.com/users/profiles/minecraft/$username")

                    if (response.isJsonObject) {
                        return TRIMMED_UUID
                            .matcher(response.asJsonObject.get("id").asString.replace("-", ""))
                            .replaceAll("$1-$2-$3-$4-$5")
                    }
                } catch (ex: Exception) {
                    // Ignored
                }

                return ""
            }
        }
    }

    class Microsoft(var refreshToken: String) : AltAccount("", "") {
        var accessToken: String = ""

        init {
            val microsoftAccessToken = run {
                val refreshTokenResponse = postRequest<JsonObject>(
                    XBOX_AUTH_URL,
                    XBOX_REFRESH_DATA.replaceAuthKeys() + refreshToken,
                    mapOf("Content-Type" to "application/x-www-form-urlencoded")
                )

                refreshToken = refreshTokenResponse.get("refresh_token")?.asString!!
                refreshTokenResponse.get("access_token").asString!!
            }

            val xboxLiveResponse = postRequest<JsonObject>(
                XBOX_XBL_URL,
                XBOX_XBL_DATA.replace(
                    "<rps_ticket>",
                    RPS_TICKET_RULE.replace("<access_token>", microsoftAccessToken)
                ),
                mapOf("Content-Type" to "application/json")
            )

            val xboxLiveToken = xboxLiveResponse.get("Token").asString
            val xboxLiveUserHash = xboxLiveResponse.get("DisplayClaims")
                .asJsonObject
                .getAsJsonArray("xui")
                .get(0)!!
                .asJsonObject
                .get("uhs")
                .asString

            val xstsResponse = postRequest<JsonObject>(
                XBOX_XSTS_URL,
                XBOX_XSTS_DATA.replace("<xbl_token>", xboxLiveToken),
                mapOf("Content-Type" to "application/json")
            )

            val xstsToken = xstsResponse.get("Token").asString

            val minecraftAuthResponse = postRequest<JsonObject>(
                MC_AUTH_URL,
                MC_AUTH_DATA
                    .replace("<userhash>", xboxLiveUserHash)
                    .replace("<xsts_token>", xstsToken),
                mapOf("Content-Type" to "application/json")
            )

            accessToken = minecraftAuthResponse.get("access_token").asString

            val minecraftProfileResponse = getRequest<JsonObject>(
                MC_PROFILE_URL,
                mapOf("Authorization" to "Bearer $accessToken")
            )

            username = minecraftProfileResponse.get("name").asString
            uuid = minecraftProfileResponse.get("id").asString
        }

        companion object {
            private const val CLIENT_ID = "d61d878d-79b6-455e-9b65-5d94d8416aad"
            private const val REDIRECT_URI = "http://localhost:1919/login"
            private const val SCOPE = "XboxLive.signin%20offline_access"
            private const val RPS_TICKET_RULE = "d=<access_token>"

            const val XBOX_PRE_AUTH_URL =
                "https://login.live.com/oauth20_authorize.srf?client_id=<client_id>&redirect_uri=<redirect_uri>&response_type=code&display=touch&scope=<scope>&prompt=select_account"
            const val XBOX_AUTH_URL = "https://login.live.com/oauth20_token.srf"

            private const val XBOX_XBL_URL = "https://user.auth.xboxlive.com/user/authenticate"
            private const val XBOX_XSTS_URL = "https://xsts.auth.xboxlive.com/xsts/authorize"
            private const val MC_AUTH_URL = "https://api.minecraftservices.com/authentication/login_with_xbox"
            private const val MC_PROFILE_URL = "https://api.minecraftservices.com/minecraft/profile"

            const val XBOX_AUTH_DATA =
                "client_id=<client_id>&redirect_uri=<redirect_uri>&grant_type=authorization_code&code="

            private const val XBOX_REFRESH_DATA =
                "client_id=<client_id>&scope=<scope>&grant_type=refresh_token&redirect_uri=<redirect_uri>&refresh_token="
            private const val XBOX_XBL_DATA =
                """{"Properties":{"AuthMethod":"RPS","SiteName":"user.auth.xboxlive.com","RpsTicket":"<rps_ticket>"},"RelyingParty":"http://auth.xboxlive.com","TokenType":"JWT"}"""
            private const val XBOX_XSTS_DATA =
                """{"Properties":{"SandboxId":"RETAIL","UserTokens":["<xbl_token>"]},"RelyingParty":"rp://api.minecraftservices.com/","TokenType":"JWT"}"""
            private const val MC_AUTH_DATA =
                """{"identityToken":"XBL3.0 x=<userhash>;<xsts_token>"}"""

            fun String.replaceAuthKeys() = this
                .replace("<client_id>", CLIENT_ID)
                .replace("<redirect_uri>", REDIRECT_URI)
                .replace("<scope>", SCOPE)
        }
    }
}