package org.morganb27;

import org.morganb27.helpers.TCPClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        TCPClient client = new TCPClient("challenge01.root-me.org", 52002);

        client.connect();

        String response = client.readMessage();
        if (response != null ) {
            System.out.println("Instructions: " + response);
            List<Integer> numbers = extractNumbersFromServerResponse(response);
            String output = computeAnswer(numbers);
            System.out.println("Output: " + output);
            client.sendMessage(output);
            String password = client.readMessage();
            System.out.println("Password: " + password);
        } else {
            System.out.println("No response from server.");
        }
    }

    private static String computeAnswer(List<Integer> numbers) {
        Integer first = numbers.get(1);
        Integer second = numbers.get(2);

        double result = Math.sqrt(first) * second;
        result = Math.round(result * 100.0) / 100.0;

        return String.valueOf(result);
    };

    private static List<Integer> extractNumbersFromServerResponse(String message) {
        List<Integer> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("-?\\d+");
        Matcher matcher = pattern.matcher(message);

        while(matcher.find()) {
            numbers.add(Integer.valueOf(matcher.group()));
        }
        return numbers;
    }
}