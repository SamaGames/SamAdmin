package net.samagames.samadmin.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * This file is part of SamAdmin.
 *
 * SamAdmin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SamAdmin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SamAdmin.  If not, see <http://www.gnu.org/licenses/>.
 */
public class CryptUtils
{
    public static String toSHA1(String text)
    {
        return crypt(text, "SHA-1");
    }

    public static String toSHA256(String text)
    {
        return crypt(text, "SHA-256");
    }

    public static String toMD5(String text)
    {
        return crypt(text, "MD5");
    }

    private static String crypt(String text, String type)
    {
        String output = "";

        try
        {
            MessageDigest digest = MessageDigest.getInstance(type);
            digest.update(text.getBytes("UTF-8"));

            byte[] hash = digest.digest();
            BigInteger bigInt = new BigInteger(1, hash);
            output = bigInt.toString(16);

            while (output.length() < 32)
            {
                output = "0" + output;
            }
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException ignored) {}

        return output;
    }
}
