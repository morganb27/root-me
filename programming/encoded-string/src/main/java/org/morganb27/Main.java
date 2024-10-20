package org.morganb27;

import org.morganb27.helpers.TCPClient;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        TCPClient client = new TCPClient("challenge01.root-me.org", 52023);

        client.connect();

        String response = client.readMessage();

        System.out.println("Response: " + response);

        if (response != null) {
            String decodedMessage = processServerResponse(response);
            if (decodedMessage != null) {
                client.sendMessage(decodedMessage);
                String password = client.readMessage();
                System.out.println(password);
            }
        }
    }

    private static String processServerResponse(String response) {
        try {
            String encodedString = extractStringFromServerResponse(response);
            return decodeBase64(encodedString);
        } catch (IllegalArgumentException e) {
            System.err.println("Failed to decode Base64: " + e.getMessage());
            return null;
        }
    }

    private static String decodeBase64(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }

    private static String extractStringFromServerResponse(String message) {
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("'([^']*)'");
        Matcher matcher = pattern.matcher(message);

        while(matcher.find()) {
            result.append(matcher.group(1));
        }
        return result.toString();
    }
}