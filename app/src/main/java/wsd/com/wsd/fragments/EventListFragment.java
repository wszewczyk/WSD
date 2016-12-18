package wsd.com.wsd.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import wsd.com.wsd.R;
import wsd.com.wsd.adapters.RecyclerViewAdapter;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.repositories.EventEntityRepository;
import wsd.com.wsd.repositories.EventEntityRepositoryImpl;

public class EventListFragment extends Fragment {

    Context context;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    String[] subjects;
    EventEntityRepository repository;

    public EventListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
//        setContentView(R.layout.activity_main);

        repository = EventEntityRepositoryImpl.getInstance();

        List<Event> events = repository.getAllEvents();

        subjects = new String[events.size()];

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        for(int i =0; i< events.size(); i++)
        {
            Date date = events.get(i).getDate();
            subjects[i] = "Name: " + events.get(i).getName() + ", Date: " + formatter.format(date);
        }

        context = getActivity().getApplicationContext();

        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativelayout1);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview1);

        recylerViewLayoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(recylerViewLayoutManager);

        recyclerViewAdapter = new RecyclerViewAdapter(context, subjects);

        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
