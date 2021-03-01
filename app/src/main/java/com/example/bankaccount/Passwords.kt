package com.example.bankaccount

import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


class Passwords {

    private val RANDOM: Random = SecureRandom()
    private val ITERATIONS = 10000
    private val KEY_LENGTH = 256

    /**
     * static utility class
     */
    private fun Passwords() {}

    /**
     * Returns a random salt to be used to hash a password.
     *
     * @return a 16 bytes random salt
     */
    val nextSalt: ByteArray?
        get() {
            val salt = ByteArray(16)
            RANDOM.nextBytes(salt)
            return salt
        }

    /**
     * Returns a salted and hashed password using the provided hash.<br></br>
     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
     *
     * @param password the password to be hashed
     * @param salt     a 16 bytes salt, ideally obtained with the getNextSalt method
     * @return the hashed password with a pinch of salt
     */
    fun hash(password: CharArray?, salt: ByteArray?): ByteArray {
        val spec = PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH)
        Arrays.fill(password, Character.MIN_VALUE)
        return try {
            val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            skf.generateSecret(spec).encoded
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError("Error while hashing a password: " + e.message, e)
        } catch (e: InvalidKeySpecException) {
            throw AssertionError("Error while hashing a password: " + e.message, e)
        } finally {
            spec.clearPassword()
        }
    }

    /**
     * Returns true if the given password and salt match the hashed value, false otherwise.<br></br>
     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
     *
     * @param password     the password to check
     * @param salt         the salt used to hash the password
     * @param expectedHash the expected hashed value of the password
     * @return true if the given password and salt match the hashed value, false otherwise
     */
    fun isExpectedPassword(password: CharArray?, salt: ByteArray?, expectedHash: ByteArray): Boolean {
        val pwdHash = hash(password, salt)
        Arrays.fill(password, Character.MIN_VALUE)
        if (pwdHash.size != expectedHash.size) return false
        for (i in pwdHash.indices) {
            if (pwdHash[i] != expectedHash[i]) return false
        }
        return true
    }

    /**
     * Generates a random password of a given length, using letters and digits.
     *
     * @param length the length of the password
     * @return a random password
     */
    fun generateRandomPassword(length: Int): String? {
        val sb = StringBuilder(length)
        for (i in 0 until length) {
            val c = RANDOM.nextInt(62)
            if (c <= 9) {
                sb.append(c)
            } else if (c < 36) {
                sb.append(('a'.toInt() + c - 10).toChar())
            } else {
                sb.append(('A'.toInt() + c - 36).toChar())
            }
        }
        return sb.toString()
    }
}