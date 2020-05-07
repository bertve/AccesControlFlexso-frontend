package com.flexso.flexsame.services
import android.os.Build
import android.provider.Settings.Secure;
import android.util.Log
import androidx.annotation.RequiresApi
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.flexso.flexsame.models.KeyRequest
import com.flexso.flexsame.models.KeyTokenData
import com.flexso.flexsame.utils.KeyPairGeneratorRSA256
import com.google.gson.Gson
import java.security.PublicKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*


object CurrentKey {
    var gson : Gson = Gson()
    var currentKeyToken: String = ""
    var currentKeyRequest : KeyRequest? = null
    var userId : Long = -1
    var officeId : Long = -1
    var deviceId : String = ""

    fun generateToken(){
        val generatorRSA256 = KeyPairGeneratorRSA256()
        val data = KeyTokenData(userId, officeId, deviceId)
        val publicKeyString = Base64.getEncoder().encodeToString(generatorRSA256.publicKey.encoded)
        currentKeyRequest = KeyRequest(userId, officeId, deviceId,publicKeyString)
        val now : Date = Date()
        val expireDate : Date = Date(now.time + 1800000)

        val privateKey = generatorRSA256.privateKey as RSAPrivateKey
        val publicKey = generatorRSA256.publicKey as RSAPublicKey

        try {
            val algorithm : Algorithm = Algorithm.RSA256(publicKey,privateKey)
            currentKeyToken = JWT.create()
                    .withIssuer("flexsame")
                    .withSubject(gson.toJson(data))
                    .withIssuedAt(now)
                    .withExpiresAt(expireDate)
                    .sign(algorithm)

        }catch (ex : JWTCreationException){
            Log.i("create_token",ex.message)
        }
    }
}