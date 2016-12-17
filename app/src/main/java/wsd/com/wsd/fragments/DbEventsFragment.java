package wsd.com.wsd.fragments;

/**
 * Created by wmsze on 13.12.2016.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;
import wsd.com.wsd.R;
import wsd.com.wsd.adapters.CustomAdapter;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.Localization;
import wsd.com.wsd.models.TimeSlot;
import wsd.com.wsd.models.types.Interwal;
import wsd.com.wsd.repositories.EventEntityRepository;
import wsd.com.wsd.repositories.EventEntityRepositoryImpl;
import wsd.com.wsd.view.models.Model;

public class DbEventsFragment extends Fragment {


    Button b1, b2, b3, b4, save;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    ListView lv;
    Model[] blootoothItems;
    TextView dateText;
    EditText title, slot;
    Date date;
    public DbEventsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_db_event, container, false);

        b1 = (Button) rootView.findViewById(R.id.button);
        b2 = (Button) rootView.findViewById(R.id.button2);
        b3 = (Button) rootView.findViewById(R.id.button3);
        b4 = (Button) rootView.findViewById(R.id.button4);
        save = (Button) rootView.findViewById(R.id.save);

        title = (EditText) rootView.findViewById(R.id.title);
        slot = (EditText) rootView.findViewById(R.id.slot);

        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView) rootView.findViewById(R.id.listView);

        dateText = (TextView) rootView.findViewById(R.id.date);

        dateText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Calendar calendar = Calendar.getInstance();
                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                        dateText.setText(year + "-" + monthOfYear + "-" + "-" + dayOfMonth);
                                        date = new Date(year, monthOfYear, dayOfMonth);
                                    }
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                        );
                        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                        break;
                }
                return true;
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                on(view);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visible(view);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list(view);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                off(view);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });
        return rootView;
    }

    public void on(View v) {
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getActivity().getApplicationContext(), "Turned on", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void off(View v) {
        BA.disable();
        Toast.makeText(getActivity().getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
    }


    public void visible(View v) {
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }


    public void list(View v) {
        pairedDevices = BA.getBondedDevices();
        List<Model> listModel = new ArrayList<Model>();

        for (BluetoothDevice bt : pairedDevices) listModel.add(new Model(bt.getName(), 0));
        Toast.makeText(getActivity().getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();
        blootoothItems = new Model[listModel.size()];
        for (int i = 0; i < listModel.size(); i++) {
            blootoothItems[i] = listModel.get(i);
        }
        CustomAdapter adapter = new CustomAdapter(getActivity(), blootoothItems);
        lv.setAdapter(adapter);
    }

    public void save(View v) {
        Event event = new Event();
        event.setName(title.getText().toString());
        event.setDescription("Description");
        event.setDate(date);
        event.setLocalization(new Localization(0,0));
        event.setTimeSlot(new TimeSlot(Interwal._10,Interwal._12));

        EventEntityRepository repository = EventEntityRepositoryImpl.getInstance();
        repository.save(event);

        List<Event> events = repository.getAllEvents();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}