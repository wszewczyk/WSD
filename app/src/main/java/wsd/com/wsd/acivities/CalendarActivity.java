package wsd.com.wsd.acivities;

/**
 * Created by wmsze on 13.12.2016.
 */

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.calendar.CalendarScopes;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.model.*;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.*;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import wsd.com.wsd.R;
import wsd.com.wsd.models.*;
import wsd.com.wsd.models.types.Interwal;

public class CalendarActivity extends Activity implements EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    private Button mCallApiButton;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout activityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        activityLayout.setLayoutParams(lp);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 16);

        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mCallApiButton = new Button(this);
        mCallApiButton.setText(BUTTON_TEXT);
        mCallApiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallApiButton.setEnabled(false);
                mOutputText.setText("");
                getResultsFromApi();
                mCallApiButton.setEnabled(true);
            }
        });
        activityLayout.addView(mCallApiButton);

        mOutputText = new TextView(this);
        mOutputText.setLayoutParams(tlp);
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        mOutputText.setText(
                "Click the \'" + BUTTON_TEXT +"\' button to test the API.");
        activityLayout.addView(mOutputText);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Calendar API ...");

        setContentView(activityLayout);

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }



    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        }
        else if (mCredential.getSelectedAccountName() == null)
        {
            chooseAccount();
        }
        else if (! isDeviceOnline())
        {
            mOutputText.setText("No network connection available.");
        }
        else
        {
            //new MakeRequestTask(mCredential).execute();
            new GetEventsWithinTimespanAsyncTask(mCredential,(new GregorianCalendar(2016,11,10)).getTime(),(new GregorianCalendar(2016,11,31)).getTime()).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    mOutputText.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                CalendarActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    public class GetEventsWithinTimespanAsyncTask extends AsyncTask<Void,Void,List<wsd.com.wsd.models.Event>>
    {
        private com.google.api.services.calendar.Calendar mService = null;
        java.util.Calendar dateFrom = null, dateTo = null;
        List<wsd.com.wsd.models.Event> returnedList = null;

        public GetEventsWithinTimespanAsyncTask(GoogleAccountCredential credential, Date from, Date to)
        {
            dateFrom = new GregorianCalendar();
            dateTo = new GregorianCalendar();
            dateFrom.setTime(from);
            dateTo.setTime(to);
            dateFrom.set(java.util.Calendar.HOUR_OF_DAY,0); //Bede uwzglednial eventy od polnocy pierwszego dnia do 23:59 dnia ostatniego
            dateFrom.set(java.util.Calendar.MINUTE,0);
            dateTo.set(java.util.Calendar.HOUR_OF_DAY,23);
            dateTo.set(java.util.Calendar.MINUTE,59);

            returnedList = new ArrayList<>();

            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(getApplicationContext().getResources().getString(R.string.app_name))
                    .build();
        }

        @Override
        protected List<wsd.com.wsd.models.Event> doInBackground(Void... voids)
        {
            try
            {
                getDataFromApi();
                return returnedList;
            }
            catch (UserRecoverableAuthIOException e1)
            {
                startActivityForResult(e1.getIntent(), REQUEST_AUTHORIZATION);
                cancel(true);
                return null;
            }
            catch (Exception e)
            {
                cancel(true);
                return null;
            }
        }

        private void getDataFromApi() throws IOException
        {
            DateTime timeFrom = new DateTime(dateFrom.getTime());
            DateTime timeTo = new DateTime(dateTo.getTime());

            Events events = mService.events().list("primary").setOrderBy("startTime").setTimeMin(timeFrom).setTimeMax(timeTo).setSingleEvents(true).execute();
            List<Event> listOfEvents = events.getItems();

            if(listOfEvents != null)
            {
                for(Event event: listOfEvents)
                {
                    java.util.Calendar eventStartTime = new GregorianCalendar();
                    eventStartTime.setTime(new Date(event.getStart().getDateTime().getValue()));
                    java.util.Calendar eventEndTime = new GregorianCalendar();
                    eventEndTime.setTime(new Date(event.getEnd().getDateTime().getValue()));

                    if(eventStartTime.get(java.util.Calendar.DAY_OF_MONTH) == eventEndTime.get(java.util.Calendar.DAY_OF_MONTH))
                    {
                        //Spotkanie konczy sie i zaczyna tego samego dnia
                        //
                        //Sprawdzam czy poczatek spotkania nie znajduje sie wczesniej niz godzina 8
                        //Sprawdzam czy koniec spotkania nie znajduje sie pozniej niz godzina 20
                        Interwal beginInterwal = null;
                        Interwal endInterwal = null;
                        if(eventStartTime.get(java.util.Calendar.HOUR_OF_DAY) >= 8)
                        {
                            if((eventEndTime.get(java.util.Calendar.HOUR_OF_DAY) <= 20) && (eventStartTime.get(java.util.Calendar.HOUR_OF_DAY) <= 20))
                            {
                                //Cale spotkanie miesci sie w przedziale 8-20 (tylko taki nas interesuje)
                                beginInterwal = Interwal.getPreviousInterwal(eventStartTime.get(java.util.Calendar.HOUR_OF_DAY),eventStartTime.get(java.util.Calendar.MINUTE));
                                endInterwal = Interwal.getNextInterwal(eventEndTime.get(java.util.Calendar.HOUR_OF_DAY),eventEndTime.get(java.util.Calendar.MINUTE));
                            }
                            else if(eventStartTime.get(java.util.Calendar.HOUR_OF_DAY) <= 20)
                            {
                                //Obcinam koncowke spotkania do godziny 20
                                beginInterwal = Interwal.getPreviousInterwal(eventStartTime.get(java.util.Calendar.HOUR_OF_DAY),eventStartTime.get(java.util.Calendar.MINUTE));
                                endInterwal = Interwal.getNextInterwal(20,0);
                            }
                            else
                            {
                                beginInterwal = null;
                                endInterwal = null;
                            }
                        }
                        else
                        {
                            if((eventEndTime.get(java.util.Calendar.HOUR_OF_DAY) <= 20) && (eventEndTime.get(java.util.Calendar.HOUR_OF_DAY) >= 8))
                            {
                                //Obcinam poczatek spotkania do godziny 8
                                beginInterwal = Interwal.getPreviousInterwal(8,0);
                                endInterwal = Interwal.getNextInterwal(eventEndTime.get(java.util.Calendar.HOUR_OF_DAY),eventEndTime.get(java.util.Calendar.MINUTE));
                            }
                            else if(eventEndTime.get(java.util.Calendar.HOUR_OF_DAY) >= 8)
                            {
                                //Obcinam spotkanie z obu stron
                                beginInterwal = Interwal.getPreviousInterwal(8,0);
                                endInterwal = Interwal.getNextInterwal(20,0);
                            }
                            else
                            {
                                beginInterwal = null;
                                endInterwal = null;
                            }
                        }

                        if((beginInterwal != null) && (endInterwal != null))
                        {
                            returnedList.add(new wsd.com.wsd.models.Event(event.getSummary() != null ? event.getSummary() : event.getId(),
                                    event.getDescription(), eventStartTime.getTime(),
                                    new TimeSlot(beginInterwal, endInterwal), new Localization(event.getLocation() != null ? event.getLocation() : "noLocalization")));
                        }
                    }
                    else
                    {
                        //Poczatek i koniec spotkania w roznych dniach
                        //
                        //Zakladamy ze takich spotkan w kalendarzach nie bedzie
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(List<wsd.com.wsd.models.Event> events)
        {
            //TODO Stad przechwycic zwrocona liste obiektow klasy Event
            if(events != null)
            {
                for(wsd.com.wsd.models.Event ev: events)
                {

                }
            }
        }
    }


    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }


        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }


        private List<String> getDataFromApi() throws IOException {
            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
            List<String> eventStrings = new ArrayList<String>();
            Events events = mService.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();

            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start = event.getStart().getDate();
                }
                eventStrings.add(
                        String.format("%s (%s)", event.getSummary(), start));
            }
            return eventStrings;
        }


        @Override
        protected void onPreExecute() {
            mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output)
        {
            mProgress.hide();
            if (output == null || output.size() == 0)
            {
                mOutputText.setText("No results returned.");
            }
            else
            {
                output.add(0, "Data retrieved using the Google Calendar API:");
                mOutputText.setText(TextUtils.join("\n", output));
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            CalendarActivity.REQUEST_AUTHORIZATION);
                } else {
                    mOutputText.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                mOutputText.setText("Request cancelled.");
            }
        }
    }
}