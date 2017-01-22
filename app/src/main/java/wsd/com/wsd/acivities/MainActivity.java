package wsd.com.wsd.acivities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;


import wsd.com.wsd.R;
import wsd.com.wsd.adapters.CustomAdapter;
import wsd.com.wsd.adapters.DrawerItemCustomAdapter;
import wsd.com.wsd.agents.CommunicationAgent;
import wsd.com.wsd.app.WSD;
import wsd.com.wsd.comunication.AgentMessage;
import wsd.com.wsd.comunication.BluetoothCommunicationService;
import wsd.com.wsd.comunication.CommunicatParser;
import wsd.com.wsd.comunication.MessageFactory;
import wsd.com.wsd.dto.NetworkInfoDto;
import wsd.com.wsd.fragments.ConnectFragment;
import wsd.com.wsd.fragments.DbEventsFragment;
import wsd.com.wsd.fragments.EventListFragment;
import wsd.com.wsd.hard.HardMemory;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.UserDevice;
import wsd.com.wsd.singletons.DeviceSingleton;
import wsd.com.wsd.singletons.EventSourceSingleton;
import wsd.com.wsd.view.models.DataModel;
import wsd.com.wsd.view.models.Model;

public class MainActivity extends AppCompatActivity {

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    int index = 1;

    UserDevice master = new UserDevice("master", 1, "C4:43:8F:92:69:9D");
    UserDevice device_one = new UserDevice("device_one", 2, "D0:51:62:5A:8E:49");
    UserDevice device_two = new UserDevice("device_two", 3, "98:D6:F7:B1:EC:9F");

    List<UserDevice> deviceList = Arrays.asList(
            master,
            device_one,
            device_two
    );

    private final Handler mHandler = new Handler() { //TODO: hardcoded
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothCommunicationService.MESSAGE_CONFIRM:
                    if (DeviceSingleton.getInstance().isCoordinator()) {
                        switch (index) {
                            case 1: {
                                final NetworkInfoDto deviceNetworkOne = new NetworkInfoDto(device_two, deviceList);
                                String message = CommunicatParser.convertACLMessagoToJSON(MessageFactory.networkPropagationMessage(deviceNetworkOne, 1111));
                                WSD.getBcs().sendMessage(device_two.getAddress(), message);
                                break;
                            }
                            case 2: {
                                startAlgorithm();
                                break;
                            }
                        }
                        index += 1;
                    }
                    break;
                case BluetoothCommunicationService.MESSAGE_FINISH:
                    if (DeviceSingleton.getInstance().isCoordinator()) {
                        switch (index) {
                            case 1: {
                                final Event event = EventSourceSingleton.getInstance().getAcceptedEvent();
                                String message = CommunicatParser.convertACLMessagoToJSON(MessageFactory.eventConfirmMessageAndStop(event, 3, 3003));
                                WSD.getBcs().sendMessage(DeviceSingleton.getInstance().getMacAddressByDeviceId(3), message);
                                break;
                            }
                            case 2: {
                                Log.e("HEHEHE", "TO JUZ KONIEC");
                                break;
                            }
                        }
                        index += 1;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Drawer
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        DataModel[] drawerItem = new DataModel[4];

        drawerItem[0] = new DataModel("Connect");
        drawerItem[1] = new DataModel("Calendar");
        drawerItem[2] = new DataModel("Add event");
        drawerItem[3] = new DataModel("Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        DeviceSingleton.getInstance().updateInfo(deviceList, master);
        WSD.setMessageHandler(mHandler);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new ConnectFragment();
                break;
            case 1:
//                fragment = new ConnectFragment();
                Intent myIntent = new Intent(MainActivity.this, CalendarActivity.class);
                MainActivity.this.startActivity(myIntent);
                break;
            case 2:
                fragment = new DbEventsFragment();
                break;
            case 3:
//                fragment = new EventListFragment();

                System.out.println("hehe wanczam");

                final NetworkInfoDto deviceNetworkOne = new NetworkInfoDto(device_one, deviceList);

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        String message = CommunicatParser.convertACLMessagoToJSON(MessageFactory.networkPropagationMessage(deviceNetworkOne, 1111));
                        WSD.getBcs().sendMessage(device_one.getAddress(), message);
                    }
                }, 1000);

                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private void startAlgorithm() { // TODO: punkt 5 - algorytm
        Log.e("Hehehe", "punkt 5 - algorytm");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String message = CommunicatParser.convertACLMessagoToJSON(MessageFactory.proposalEventMessage(1, 2, 1111, HardMemory.getMemoryByDeviceId(1).get(0)));
                WSD.getBcs().sendMessage(device_one.getAddress(), message);
            }
        }, 1000);

    }

}