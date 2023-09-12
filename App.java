import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class App {
    public static void main(String[] args) throws Exception {
        
        HashMap<Integer, String> currencyCodes = new HashMap<Integer,String>();
        boolean conditional = true;
        System.out.println("Welcome to Currency Converter");

        currencyCodes.put(1, "USD");
        currencyCodes.put(2, "PKR");
        currencyCodes.put(3, "INR");
        currencyCodes.put(4, "CAD");
        currencyCodes.put(5, "AED");

        
        String fromCode, toCode;
        double amount;
        Scanner sc = new Scanner(System.in);
        do{
        
        
        System.out.println("Please Select the Initial Currency (FROM):-");
        System.out.println("1: USD \n 2: PKR \n 3: INR \n 4: CAD \n 5: AED");

        // Getting the Currency Code of Initital Currency 
        System.out.println("Select the Base(Initial) Currency from which you want to convert:-");
        int from = sc.nextInt();
        while(from>5 || from<1){
            System.out.println("Please select the valid number (1 to 5 only)");
            from = sc.nextInt();
        }
        fromCode = currencyCodes.get(from);
        System.out.println("Select the Target Currency in which you want to convert:-");
        // Getting the Currency Code of Target Currency 
        int to = sc.nextInt();
        while(to>5 || to<1 || to == from){
            System.out.println("Please select the valid number and not the Same Target Currency(1 to 5 only)");
            to = sc.nextInt();
        }
        toCode = currencyCodes.get(to);
        
        //Getting the Amount 
        System.out.println("Insert the Amount");
        amount = sc.nextDouble();

        sendHttpGetRequest(fromCode, toCode, amount);

        // ASking user if we want to use app again
        System.out.println("Do You want to Convert again\nPress 1 for Yes and 0 for No");
        int num = sc.nextInt();
      
        if(num == 0){
            conditional = false;
        }
        else;
        
    }while(conditional == true);
        sc.close();
    }
    private static void sendHttpGetRequest(String fromCode, String toCode, double amount) throws IOException, JSONException{
        // accessCode = API KEY
        
        // I Will Remove the API KEY AKA accessCode before Uploading the Code

        String accessCode = "";
       
        // fixar.io didn't work using exchangeRate

        String GET_URL = "https://v6.exchangerate-api.com/v6/"+accessCode+"/latest/"+fromCode;
        URL url = new URL(GET_URL);
        //Opening Connection
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setRequestMethod("GET");

        int ResponseCode = httpConnection.getResponseCode();

        if(ResponseCode == HttpURLConnection.HTTP_OK){ //Succesfully made connection and GET Request
            System.out.println("Succesfully fetched Real Time Conversion Rates");

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            String inputLines;
            StringBuffer response = new StringBuffer();

            while((inputLines = reader.readLine()) != null){
                response.append(inputLines);
            }
            reader.close();
            JSONObject obj = new JSONObject(response.toString());
            Double exchangeRate = obj.getJSONObject("conversion_rates").getDouble(toCode);
            System.out.println(obj.getJSONObject("conversion_rates"));
            System.out.println(exchangeRate+"\n"); //checking
            //Conversion
            System.out.println(amount +" "+fromCode+"  =  "+(amount*exchangeRate)+" "+toCode);
            
        }
        else{
            System.out.println("The fetch of exchange Rate is unsuccessful, GET Request Failed");
        }

    }
}
