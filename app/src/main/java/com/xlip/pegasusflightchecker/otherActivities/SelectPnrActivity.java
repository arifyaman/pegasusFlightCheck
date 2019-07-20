package com.xlip.pegasusflightchecker.otherActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xlip.pegasusflightchecker.R;
import com.xlip.pegasusflightchecker.httpClient.HttpClient;
import com.xlip.pegasusflightchecker.model.AvailabilityResponse;
import com.xlip.pegasusflightchecker.model.Fare;
import com.xlip.pegasusflightchecker.model.Flight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectPnrActivity extends AppCompatActivity implements HttpClient.HttpClientCallbacks {
    private String pnrId;
    private String pnrNo;
    private String surname;
    private String passengerId;
    private String paymentOptionIdentifier;
    private String paymentAmount;
    private LinearLayout scrollView;
    private LinearLayout cc_paymentLayout;
    private Button payButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pnr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scrollView = findViewById(R.id.slct_pnr_scroll_view);
        payButton = findViewById(R.id.btn_pay);
        payButton.setOnClickListener(view -> {
            pay();
        });
        cc_paymentLayout = findViewById(R.id.scr_cc_payment_layout);
        cc_paymentLayout.setVisibility(View.GONE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                new Thread(() -> {
                    HttpClient.testPay(SelectPnrActivity.this);
                }).start();

            }
        });

        AvailabilityResponse response = ((AvailabilityResponse) getIntent().getSerializableExtra("flightList"));

        for (Flight flight : response.getFlights()) {
            TextView textView = new TextView(this);
            textView.setText("Flight Time: " + flight.getDepartureDateTime());
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            scrollView.addView(textView);
            for (com.xlip.pegasusflightchecker.model.Bundle bundle : flight.getFare().getBundleList()) {
                Button button = new Button(this);
                button.setText(bundle.getBundleType() + "     Amount: " + bundle.getShownFare().getAmount() + " " + bundle.getShownFare().getCurrency());
                LinearLayout.LayoutParams buttonLparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonLparams.setMargins(30, 0, 0, 0);
                button.setLayoutParams(buttonLparams);
                button.setAllCaps(false);
                String pnrReq = createPnrReqeust(flight.getFare(), bundle);

                button.setOnClickListener(view -> {
                    new Thread(() -> {
                        HttpClient.postJson("https://web.flypgs.com/pegasus/reservation/easy-pnr", pnrReq, "pnrReq", SelectPnrActivity.this);
                    }).start();
                });

                scrollView.addView(button);

            }

        }


    }

    @Override
    public void responseReceived(JSONObject response, String operation) {

        runOnUiThread(() -> {
            Log.d("PNRActivity", "Response: " + response.toString());
            Log.d("PNRActivity", "operation: " + operation);

            if (operation.equals("pnrReq")) {
                try {
                    this.pnrId = response.getString("pnrId");
                    this.passengerId = response.getJSONArray("passengerSummaryList").getJSONObject(0).getString("id");

                    this.scrollView.removeAllViews();
                    TextView textView = new TextView(this);
                    textView.setText("PNR Id: " + this.pnrId);
                    scrollView.addView(textView);

                    Button button = new Button(this);
                    button.setText("Reserve This PNR");
                    LinearLayout.LayoutParams buttonLparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    buttonLparams.setMargins(30, 0, 0, 0);
                    button.setLayoutParams(buttonLparams);
                    button.setAllCaps(false);
                    String pnrReq = createReservationRequest();

                    button.setOnClickListener(view -> {
                        new Thread(() -> {
                            HttpClient.putJson("https://web.flypgs.com/pegasus/reservation/easy-pnr", pnrReq, "reservePnr", SelectPnrActivity.this);
                        }).start();
                    });
                    scrollView.addView(button);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (operation.equals("reservePnr")) {
                try {
                    TextView textView = new TextView(this);

                    this.pnrNo = response.getString("pnrNo");
                    textView.setText("Reserved \n PNR No: " + this.pnrNo);
                    this.surname = "YAMAN";
                    scrollView.addView(textView);
                    String pnrDetailsRequest = createPnrDetailsRequest();
                    new Thread(() -> {
                        HttpClient.postJson("https://web.flypgs.com/pegasus/pnr-search/details", pnrDetailsRequest, "pnrDetails", SelectPnrActivity.this);
                    }).start();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (operation.equals("pnrDetails")) {

                try {
                    TextView textView = new TextView(this);
                    JSONObject totalFare = response.getJSONObject("totalFare");
                    String currency = totalFare.getString("currency");
                    String amount = totalFare.getString("amount");

                    this.pnrNo = response.getJSONObject("pnrInfo").getString("pnrNo");
                    textView.setText("Total Fare: " + amount + " " + currency);
                    this.paymentAmount = amount;
                    this.surname = "YAMAN";
                    scrollView.addView(textView);

                    Button button = new Button(this);
                    button.setText("Get Payment Options");

                    button.setOnClickListener(view -> {
                        String paymentRequest = createPaymentOptionsRequest();

                        new Thread(() -> {
                            HttpClient.postJson("https://web.flypgs.com/pegasus/payment/all-payment-options", paymentRequest, "paymentOptions", SelectPnrActivity.this);
                        }).start();

                    });
                    scrollView.addView(button);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (operation.equals("paymentOptions")) {
                try {
                    JSONArray paymentList = response.getJSONArray("paymentOptionList");
                    for (int i = 0; i < paymentList.length(); i++) {
                        JSONObject payment = paymentList.getJSONObject(i);
                        try {
                            if (payment.getJSONObject("optionName").getString("key").equals("payment.paymentOptionLabel.UP")) {
                                paymentOptionIdentifier = payment.getString("optionIdentifier");
                                cc_paymentLayout.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (operation.equals("pay")) {


                try {
                    TextView orderId = new TextView(this);
                    orderId.setText("Order Id: " + response.getString("orderId"));
                    scrollView.addView(orderId);

                    TextView redirectGatewayUrl = new TextView(this);
                    redirectGatewayUrl.setText("Redirect Gateway Url: " + response.getString("redirectGatewayUrl"));
                    scrollView.addView(redirectGatewayUrl);

                    TextView redirectRequest = new TextView(this);
                    redirectGatewayUrl.setText("Redirect Request: " + response.getString("redirectRequest"));
                    scrollView.addView(redirectRequest);


                    scrollView.removeAllViews();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });


    }

    private void pay() {
        String payRequest = createPayRequest();

        new Thread(() -> {
            HttpClient.postJson("https://web.flypgs.com/pegasus/payment/complete", payRequest, "pay", SelectPnrActivity.this);
        }).start();
    }


    private String createPnrReqeust(Fare fare, com.xlip.pegasusflightchecker.model.Bundle bundle) {
        String reqJson = "{\n  \"flightPnrList\": [\n    {\n      \"addOn\": false,\n      \"bundleId\": \"#bundleId#\",\n      \"departureFlight\": true,\n      \"fareBasisCode\": \"GWEB/INT\",\n      \"reservationClass\": \"G\",\n      \"segmentId\": \"#segmentId#\",\n      \"tariffFareId\": \"#tariffFareId#\",\n      \"addFlex\": false\n    }\n  ],\n  \"adultCount\": 1,\n  \"childCount\": 0,\n  \"infantCount\": 0,\n  \"soldierCount\": 0,\n  \"affiliate\": {},\n  \"ffRedemption\": false\n}";
        reqJson = reqJson.replaceAll("#bundleId#", bundle.getBundleId()).replaceAll("#segmentId#", fare.getSegmentId()).replaceAll("#tariffFareId#", fare.getTariffFareId());
        return reqJson;
    }

    private String createPnrDetailsRequest() {
        String reqJson = "{\"pnrNo\":\"#pnrNo#\",\"surname\":\"#surname#\",\"pnrId\":\"#pnrId#\",\"currency\":\"USD\",\"smsSelected\":false,\"insuranceSelected\":null,\"operationType\":\"SellPnr\"}";
        reqJson = reqJson.replaceAll("#pnrNo#", pnrNo).replaceAll("#pnrId#", pnrId).replaceAll("#surname#", this.surname);
        return reqJson;
    }


    private String createReservationRequest() {
        String reqJson = "{\"pnrId\":\"#pnrId#\",\"passengerList\":[{\"birthDate\":\"1994-07-25\",\"nationalId\":\"34780337858\",\"gender\":\"M\",\"name\":\"ARIF\",\"surname\":\"YAMAN\",\"passengerId\":\"#passengerId#\",\"createFFMemberContact\":false,\"ffId\":\"905433214929\"}],\"currency\":\"USD\",\"smsSelected\":false,\"emailAllowedForMarketing\":false,\"phoneAllowedForMarketing\":false,\"personnelPnr\":false,\"contactPersonDetailed\":{\"name\":\"ARIF\",\"surname\":\"YAMAN\",\"countryCode\":\"TR\",\"email\":\"arifymn@hotmail.com\",\"phoneNumber\":{\"countryCode\":\"90\",\"areaCode\":\"543\",\"number\":\"3214929\"}}}";
        reqJson = reqJson.replaceAll("#pnrId#", pnrId);
        reqJson = reqJson.replaceAll("#passengerId#", passengerId);
        return reqJson;
    }

    private String createPaymentOptionsRequest() {
        String reqJson = "{\"currency\":\"USD\",\"operationType\":\"SellPnr\",\"pnrId\":\"#pnrId#\"}";
        reqJson = reqJson.replaceAll("#pnrId#", pnrId);
        return reqJson;
    }

    private String createPayRequest() {
        String reqJson = "{\"creditCardPayment\":{\"optionIdentifier\":\"#paymentOptionIdentifier#\",\"holder\":\"ALİ UZUNYOL\",\"number\":\"4111111111111111\",\"expireDateMonth\":\"04\",\"expireDateYear\":\"2022\",\"cvv\":\"202\",\"fee\":{\"currency\":\"USD\",\"amount\":#paymentAmount#}},\"pnrId\":\"#pnrId#\",\"pnrNo\":\"#pnrNo#\",\"surName\":\"#surname#\",\"operationType\":\"SellPnr\",\"optionExtensionId\":null,\"smsSelected\":false}";
        reqJson = reqJson.replaceAll("#pnrId#", pnrId);
        reqJson = reqJson.replaceAll("#pnrNo#", pnrNo);
        reqJson = reqJson.replaceAll("#surname#", surname);
        reqJson = reqJson.replaceAll("#paymentOptionIdentifier#", paymentOptionIdentifier);
        reqJson = reqJson.replaceAll("#paymentAmount#", paymentAmount);
        return reqJson;
    }


}
