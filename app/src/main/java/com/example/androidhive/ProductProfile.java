package com.example.androidhive;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jivanpatil on 4/30/2015.
 */
public class ProductProfile extends Activity {
    TextView txtName;
    TextView txtNumber;
    TextView txtDesc;
    ImageView propicUrl;
    ImageView propicUrl1;
    ImageView propicUrl2;
    EditText txtCreatedAt;





    // Image Buttons

    public String pid,propicurl,site,number,location,email;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    private static final String url_product_detials = "http://10.0.2.2/android_connectv2/get_product_details.php";

    // url to update product
    private static final String url_update_product = "http://10.0.2.2/android_connectv2/update_product.php";

    // url to delete product
    private static final String url_delete_product = "http://10.0.2.2/android_connectv2/delete_product.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_NUMBER = "number";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_PROPICURL = "propicurl";


    //private static final String TAG_SITE ="site";
    //private static final String TAG_EMAIL ="email";
    //private static final String TAG_LOCATION ="location";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_profile);
        // save button

        ImageView tabButton5 = (ImageView) findViewById(R.id.imgTabIcon5);
        ImageView tabButton4 = (ImageView) findViewById(R.id.imgTabIcon4);
        ImageView tabButton3 = (ImageView) findViewById(R.id.imgTabIcon3);
        ImageView tabButton2 = (ImageView) findViewById(R.id.imgTabIcon2);
        ImageView tabButton1 = (ImageView) findViewById(R.id.imgTabIcon1);



        // getting product details from intent
        final Intent i = getIntent();

        // getting product id (pid) from intent
        pid = i.getStringExtra(TAG_PID);


        //propicurl = i.getStringExtra(TAG_PROPICURL);
        // Getting complete product details in background thread
        new GetProductDetails().execute();

        tabButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(site));
                startActivity(intent);
            }
        });



        tabButton3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:123456789"));
                startActivity(callIntent);



            }
        });

        tabButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent in = new Intent(getApplicationContext(), SMSActivity.class);
                startActivity(in);

            }
        });


        tabButton2.setOnClickListener(new View.OnClickListener() {
            Intent intentmsg1=null,chooser1=null;
            @Override
            public void onClick(View view) {
                // Launching All products Activity
                intentmsg1=new Intent(Intent.ACTION_SEND);
                intentmsg1.setData(Uri.parse("mailto:"));
                String [] to={email};
                intentmsg1.putExtra(Intent.EXTRA_EMAIL,to);
                intentmsg1.putExtra(Intent.EXTRA_SUBJECT,"Order Details");
                intentmsg1.putExtra(Intent.EXTRA_TEXT,"I miss CAS");
                intentmsg1.setType("message/rfc822");
                chooser1= intentmsg1.createChooser(intentmsg1,"Send Message");
                startActivity(chooser1);
            }
        });


        tabButton5.setOnClickListener(new View.OnClickListener() {
            Intent intentmsg=null, chooser=null;
            @Override
            public void onClick(View view) {
                // Launching All products Activity
                intentmsg=new Intent(Intent.ACTION_VIEW);
                intentmsg.setData(Uri.parse(site));
                chooser=Intent.createChooser(intentmsg,"Launch Maps");
                startActivity(chooser);

            }
        });





    }


    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProductProfile.this);
            pDialog.setMessage("Loading product details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("pid", pid));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_product_detials, "GET", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array

                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);

                            // product with this pid found
                            // Edit Text


                            txtName = (TextView) findViewById(R.id.showName);
                            txtNumber = (TextView) findViewById(R.id.showPrice);
                            txtDesc = (TextView) findViewById(R.id.showDesc);
                            propicUrl = (ImageView) findViewById(R.id.showImage);
                            propicUrl1 =(ImageView) findViewById(R.id.showImage1);
                            propicUrl2 =(ImageView) findViewById(R.id.showImage2);



                            // display product data in EditText
                            txtName.setText(product.getString(TAG_NAME));
                            txtNumber.setText(product.getString(TAG_NUMBER));
                            txtDesc.setText(product.getString(TAG_DESCRIPTION));
                            propicurl = product.getString(TAG_PROPICURL);
                            int loader = R.drawable.loader;

                            ImageLoader imgLoader = new ImageLoader(getApplicationContext());

                            imgLoader.DisplayImage(propicurl, loader, propicUrl);

                            //image_url = product.getString(TAG_PROPICURL);






                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }






}


