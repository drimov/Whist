package com.example.whist.data.network

import androidx.collection.ArrayMap
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

//private const val PATH: String = "/whist"
private const val PATH: String = "/Whist2.0"

/**
 * A retrofit service to fetch data
 */
interface WhistService {

    @GET("$PATH/room")
    fun getRoomAsync(): Deferred<NetworkTableContainer>

    @GET("$PATH/tableName/{name}")
    fun getTableAsync(@Path("name") name: String): Deferred<Int>

    @GET("$PATH/table/{id}")
    fun getTableByIdAsync(@Path("id") tableId: Int): Deferred<NetworkTableContainer>

    @GET("$PATH/fcmToken/{token}")
    fun getFcmAsync(@Path("token") token: String?): Deferred<Int>

    @GET("$PATH/fcmId/{id}")
    fun getFcmIdAsync(@Path("id") id: Int): Deferred<NetworkFcm>

    @GET("$PATH//tableexist/{name}")
    fun getTableExistAsync(@Path("name") name: String): Deferred<Boolean>

    @GET("$PATH/player/tableId/{id}")
    fun getPlayersAsync(@Path("id") id: Int): Deferred<NetworkPlayerContainer>

    @GET("$PATH/playerexist/{name}")
    fun getPlayerExistAsync(@Path("name") name: String): Deferred<Boolean>


    @FormUrlEncoded
    @POST("$PATH/newTable")
    suspend fun setNewTableAsync(
        @Field("tableName") name: String,
        @Field("pwdTable") pwd: String,
    ): Response<String>

    @FormUrlEncoded
    @POST("$PATH/newPlayer")
    suspend fun setNewPlayerAsync(
        @Field("playerName") name: String,
        @Field("tableId") tableId: Int,
        @Field("isOwner") isOwner: Boolean,
        @Field("fcmId") fcmId: Int
    ): Response<String>

    @FormUrlEncoded
    @POST("$PATH/newFcm")
    suspend fun setNewFcmAsync(
        @Field("token") token: String?,
    ): Response<Unit>

    @FormUrlEncoded
    @POST("$PATH/sendMessage")
    suspend fun sendMessageAsync(
        @Field("topic") topic: String?,
        @FieldMap data: Map<String, String>,
    ): Response<Unit>

    @FormUrlEncoded
    @POST("$PATH/deletePlayer")
    suspend fun deletePlayer(
        @Field("playerName") name: String
    ): Response<String>

    @FormUrlEncoded
    @POST("$PATH/deleteTableGame")
    suspend fun deleteTableGame(
        @Field("tableId") tableId: Int
    ): Response<String>

    @FormUrlEncoded
    @POST("$PATH/closeTable")
    suspend fun updateTable(
        @Field("tableId") tableId: Int
    ): Response<String>

}