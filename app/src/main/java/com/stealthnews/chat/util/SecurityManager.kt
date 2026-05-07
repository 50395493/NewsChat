package com.stealthnews.chat.util

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class SecurityManager(private val context: Context) {
    
    companion object {
        private const val KEY_ALIAS = "stealth_chat_key"
        private const val PREFERENCE_NAME = "secure_prefs"
        private const val ACTIVATION_CODE_KEY = "activation_code"
        private const val DEFAULT_ACTIVATION_CODE = "##stealth##"
    }
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFERENCE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }
    
    /**
     * 检查激活码是否正确
     */
    fun checkActivationCode(input: String): Boolean {
        val savedCode = sharedPreferences.getString(ACTIVATION_CODE_KEY, DEFAULT_ACTIVATION_CODE)
        return input == savedCode
    }
    
    /**
     * 设置新的激活码
     */
    fun setActivationCode(code: String) {
        sharedPreferences.edit().putString(ACTIVATION_CODE_KEY, code).apply()
    }
    
    /**
     * 加密消息内容
     */
    fun encryptMessage(message: String): String {
        return try {
            val cipher = getCipher(Cipher.ENCRYPT_MODE)
            val encryptedBytes = cipher.doFinal(message.toByteArray())
            Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            message // 如果加密失败，返回原始消息
        }
    }
    
    /**
     * 解密消息内容
     */
    fun decryptMessage(encryptedMessage: String): String {
        return try {
            val cipher = getCipher(Cipher.DECRYPT_MODE)
            val encryptedBytes = Base64.decode(encryptedMessage, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            String(decryptedBytes)
        } catch (e: Exception) {
            encryptedMessage // 如果解密失败，返回原始消息
        }
    }
    
    /**
     * 获取加密密钥
     */
    private fun getOrCreateSecretKey(): SecretKey {
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore"
            )
            
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).apply {
                setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                setKeySize(256)
            }.build()
            
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
        
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }
    
    /**
     * 获取加密/解密器
     */
    private fun getCipher(mode: Int): Cipher {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val secretKey = getOrCreateSecretKey()
        
        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(mode, secretKey)
        } else {
            val iv = sharedPreferences.getString("${KEY_ALIAS}_iv", null)
            if (iv != null) {
                val ivBytes = Base64.decode(iv, Base64.DEFAULT)
                val spec = GCMParameterSpec(128, ivBytes)
                cipher.init(mode, secretKey, spec)
            }
        }
        
        return cipher
    }
    
    /**
     * 安全删除文件
     */
    fun secureDelete(filePath: String): Boolean {
        return try {
            val file = java.io.File(filePath)
            if (file.exists()) {
                // 多次覆盖后删除
                val length = file.length()
                val random = java.util.Random()
                val fos = java.io.FileOutputStream(file)
                
                // 三次覆盖
                repeat(3) {
                    val randomBytes = ByteArray(length.toInt())
                    random.nextBytes(randomBytes)
                    fos.write(randomBytes)
                    fos.flush()
                }
                fos.close()
                
                file.delete()
            } else {
                true
            }
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 生成随机新闻标题（用于通知伪装）
     */
    fun generateRandomNewsTitle(): String {
        val titles = listOf(
            "最新财经动态",
            "科技前沿报道", 
            "体育赛事快讯",
            "娱乐八卦新闻",
            "国际时事要闻",
            "本地民生新闻",
            "健康生活资讯",
            "教育政策解读"
        )
        return titles.random()
    }
}