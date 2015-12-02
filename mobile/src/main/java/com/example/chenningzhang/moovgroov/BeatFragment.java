package com.example.chenningzhang.moovgroov;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BeatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BeatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BeatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String START_WATCH_BEATS_ACTIVITY = "/watch_beats_activity";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private GoogleApiClient mGoogleApiClient;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BeatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BeatFragment newInstance(int pos) {
        BeatFragment fragment = new BeatFragment();
        Bundle args = new Bundle();
        args.putInt("position", pos);

        fragment.setArguments(args);
        return fragment;
    }

    public BeatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d("MOBILE","SERVICE CONNECTED TO GOOGLE API");
                        /* Successfully connected */
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d("MOBILE","SERVICE SUSPEEDED FROM GOOGLE API");
                        /* Connection was interrupted */
                    }
                })
                .build();
        mGoogleApiClient.connect();

   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_beat, container, false);
        final ImageButton newButton = (ImageButton) view.findViewById(R.id.newButton);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.beatFragment, AddNewFragment.newInstance());
                fragmentTransaction.commit();

                // Send Message to Watch to start beats
                String msg = "START";
                // Send message to Phone.
                sendMessage( START_WATCH_BEATS_ACTIVITY, msg);
                Log.d("MOBILE BEAT", "Start Beat");
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    // Send message through api to Watch
    private void sendMessage( final String path, final String text ) {
        Log.d("MOBILE SEND MESSAGE", "Start Beat 1");
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mGoogleApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    Log.d("MOBILE SEND MESSAGE", "Start Beat with NODES");
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mGoogleApiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGoogleApiClient.disconnect();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
