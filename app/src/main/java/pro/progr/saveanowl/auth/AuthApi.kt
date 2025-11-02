package pro.progr.saveanowl.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/vk")
    suspend fun signIn(@Body body: AuthVkRequest): Response<AuthVkResponse>
}