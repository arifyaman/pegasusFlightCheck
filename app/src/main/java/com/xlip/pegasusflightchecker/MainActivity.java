package com.xlip.pegasusflightchecker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xlip.pegasusflightchecker.httpClient.HttpClient;
import com.xlip.pegasusflightchecker.model.AvailabilityRequest;
import com.xlip.pegasusflightchecker.model.AvailabilityResponse;
import com.xlip.pegasusflightchecker.model.Flight;
import com.xlip.pegasusflightchecker.model.FlightSearchList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements HttpClient.HttpClientCallbacks {

    private Button sendButton;
    private EditText dateInput;
    private EditText depPort;
    private EditText arPort;
    private TextView responseText;

    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gson = new Gson();

        dateInput = findViewById(R.id.dateInput);
        depPort = findViewById(R.id.depPort);
        arPort = findViewById(R.id.arPort);


        sendButton = findViewById(R.id.sendButton);
        responseText = findViewById(R.id.responseText);



        sendButton.setOnClickListener((View v) -> {
            AvailabilityRequest request = createAvailabilityReqeust(dateInput.getText().toString(), arPort.getText().toString(), depPort.getText().toString());
            String payload = gson.toJson(request);
            new Thread(() -> {
                HttpClient.getInstance().postO("https://web.flypgs.com/pegasus/availability", payload, MainActivity.this);

            }).start();
        });
    }


    private AvailabilityRequest createAvailabilityReqeust(String date, String arrivalPort, String departurePort) {
        AvailabilityRequest request = new AvailabilityRequest();


        FlightSearchList searchList = new FlightSearchList();
        searchList.setDepartureDate(date);
        //searchList.setArrivalPort("IST_SAW");
        // searchList.setDeparturePort("DOH");
        searchList.setArrivalPort(arrivalPort);
        searchList.setDeparturePort(departurePort);


        request.setFlightSearchList(new FlightSearchList[]{searchList});
        request.setAdultCount("1");
        request.setCurrency("USD");
        request.setDateOption("1");

        return request;
    }

    @Override
    public void responseReceived(JSONObject response) {
        runOnUiThread(() -> {
                    Log.d("response", response.toString());
                    try {
                        JSONArray array = response.getJSONArray("departureRouteList");
                        JSONObject depRouteList = array.getJSONObject(0);
                        JSONArray dailyFlightList = depRouteList.getJSONArray("dailyFlightList");

                        AvailabilityResponse availabilityResponse = new AvailabilityResponse();

                        String responseText = "";
                        for (int i = 0; i < dailyFlightList.length(); i++) {
                            Flight flight = new Flight();

                            JSONObject date1 = dailyFlightList.getJSONObject(i);
                            flight.setDate(date1.getString("date"));

                            JSONObject cheapestFare = date1.getJSONObject("cheapestFare");

                            flight.setAmount(cheapestFare.getDouble("amount"));
                            flight.setCurrency(cheapestFare.getString("currency"));

                            availabilityResponse.getFlights().add(flight);

                            responseText += "Date: " + flight.getDate() + "\n";
                            responseText += "Currency: " + flight.getCurrency() + "\n";
                            responseText += "Amount: " + flight.getAmount() + "\n";
                            responseText += "\n";
                        }

                        this.responseText.setText(responseText);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        );

    }
}
