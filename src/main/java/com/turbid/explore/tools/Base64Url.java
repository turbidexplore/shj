package com.turbid.explore.tools;

import org.bouncycastle.util.encoders.Base64;

public class Base64Url {

        public static byte[] base64EncodeUrl(byte[] input) {
            byte[] base64 = Base64.encode(input);
            for (int i = 0; i < base64.length; ++i)
                switch (base64[i]) {
                    case '+':
                        base64[i] = '*';
                        break;
                    case '/':
                        base64[i] = '-';
                        break;
                    case '=':
                        base64[i] = '_';
                        break;
                    default:
                        break;
                }
            return base64;
        }

        public static byte[] base64DecodeUrl(byte[] input)  {
            byte[] base64 = input.clone();
            for (int i = 0; i < base64.length; ++i)
                switch (base64[i]) {
                    case '*':
                        base64[i] = '+';
                        break;
                    case '-':
                        base64[i] = '/';
                        break;
                    case '_':
                        base64[i] = '=';
                        break;
                    default:
                        break;
                }
            return Base64.decode(base64.toString());
        }
    }