package com.group.eBookManagementSystem.gui.utlils;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestUtils {

    public static String[] sendGetRequest(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                return new String[]{String.valueOf(responseCode), response.toString()};
            } else {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String errorLine;
                StringBuffer errorResponse = new StringBuffer();

                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }

                errorReader.close();
                return new String[]{String.valueOf(responseCode), errorResponse.toString()};
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new String[]{"Error: " + e.getMessage()};
        }
    }

    public static String[] sendPostRequest(URL url) {
        return sendPostRequest(url, null);
    }

    public static String[] sendPostRequest(URL url, Object requestBody) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            if (requestBody != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                String payload = objectMapper.writeValueAsString(requestBody);
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(payload);
                out.close();
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                return new String[]{String.valueOf(responseCode), response.toString()};
            } else {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String errorLine;
                StringBuffer errorResponse = new StringBuffer();

                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }

                errorReader.close();
                return new String[]{String.valueOf(responseCode), errorResponse.toString()};
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return new String[]{"Error: " + e.getMessage()};
        }
    }

}
