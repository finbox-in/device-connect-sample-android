/**
 *  Utls
 *  DeviceConnectSample
 *  Created by Ashutosh Jena on 04/11/2025.
 */

package `in`.finbox.deviceconnectsample

import java.security.SecureRandom


object Utils {

    private val ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    private val random = SecureRandom()

    fun getCustomerId(): String {
        val sb = StringBuilder(48)
        repeat(48) {
            val index = random.nextInt(ALPHANUM.length)
            sb.append(ALPHANUM[index])
        }
        return sb.toString()
    }
}