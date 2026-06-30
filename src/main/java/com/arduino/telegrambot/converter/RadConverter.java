package com.arduino.telegrambot.converter;

public class RadConverter {

    public static String convertDigitalToBinary(int digital) {
        var stringBuilder = new StringBuilder();
        while (digital > 0) {
            stringBuilder.append(digital % 2);
            digital /= 2;
        }
        return stringBuilder.reverse().toString();
    }

    public static String convertBinaryToDigital(int binary) {

        var binaryValue = String.valueOf(Math.abs(binary));
        double result = 0;

        for (int i = 0; i < binaryValue.length(); i++) {
            int bit = binary % 10;
            if (bit != 0) {
                result += Math.pow(2, i);
            }
            binary /= 10;
        }
        return String.valueOf(result);
    }
}
