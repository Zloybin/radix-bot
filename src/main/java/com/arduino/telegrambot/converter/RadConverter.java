package com.arduino.telegrambot.converter;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.IntStream;

@Component
public class RadConverter {
    private static final Map<Integer, Character> HEX_CODE_MAP = Map.ofEntries(
            Map.entry(0, '0'),
            Map.entry(1, '1'),
            Map.entry(2, '2'),
            Map.entry(3, '3'),
            Map.entry(4, '4'),
            Map.entry(5, '5'),
            Map.entry(6, '6'),
            Map.entry(7, '7'),
            Map.entry(8, '8'),
            Map.entry(9, '9'),
            Map.entry(10, 'A'),
            Map.entry(11, 'B'),
            Map.entry(12, 'C'),
            Map.entry(13, 'D'),
            Map.entry(14, 'E'),
            Map.entry(15, 'F')

    );


    public String convertDecimalToBinary(int decimal) {
        var stringBuilder = new StringBuilder();
        while (decimal > 0) {
            stringBuilder.append(decimal % 2);
            decimal /= 2;
        }
        return stringBuilder.reverse().toString();
    }

    public String convertBinaryToDecimal(int binary) {

        var binaryValue = String.valueOf(Math.abs(binary));
        double result = 0;

        for (int i = 0; i < binaryValue.length(); i++) {
            int bit = binary % 10;
            if (bit != 0) {
                result += Math.pow(2, i);
            }
            binary /= 10;
        }
        return String.valueOf(Math.round(result));
    }

    public String convertDecimalToHex(int decimal) {

        StringBuilder hex = new StringBuilder();

        while (decimal > 0) {
            int rest = decimal % 16;
            Character c = HEX_CODE_MAP.get(rest);
            hex.insert(0, c);
            decimal /= 16;
        }
        return hex.toString();
    }

    public int convertHexToDecimal(String hex) {

        return IntStream.range(0, hex.length()).map(i -> {
            char c = hex.charAt(hex.length() - 1 - i);

            int decimal = 0;

            for (Map.Entry<Integer, Character> entry : HEX_CODE_MAP.entrySet()) {
                if (entry.getValue().equals(c)) {
                    decimal = (int) (entry.getKey() * Math.pow(16, i));
                }
            }

            return decimal;

        }).sum();

    }

    public String convertBinaryToHex(int binary) {
        var answer = new StringBuilder();
        while (binary > 0) {

            var binaryValue = String.valueOf(Math.abs(binary));
            int hexValue;
            var stringBuilder = new StringBuilder();
            if (binaryValue.length() > 4) {
                hexValue = Integer.parseInt(stringBuilder.append(binaryValue.substring(binaryValue.length() - 4)).toString());
            } else {
                hexValue = Integer.parseInt(stringBuilder.append(binaryValue).toString());
            }
            int result = 0;
            for (int i = 0; i < stringBuilder.length(); i++) {
                int bit = hexValue % 10;
                if (bit != 0) {
                    result += (int) Math.pow(2, i);
                }
                hexValue /= 10;
            }
            stringBuilder.setLength(0);
            Character c = HEX_CODE_MAP.get(result);
            answer.insert(0, c);
            binary /= 10000;
        }
        return answer.toString();
    }

    public String convertHexToBinary(String hex) {

        int dec = IntStream.range(0, hex.length()).map(i -> {
            char c = hex.charAt(hex.length() - 1 - i);

            int decimal = 0;

            for (Map.Entry<Integer, Character> entry : HEX_CODE_MAP.entrySet()) {
                if (entry.getValue().equals(c)) {
                    decimal = (int) (entry.getKey() * Math.pow(16, i));
                }

            }

            return decimal;

        }).sum();

        return convertDecimalToBinary(dec);

    }
}

