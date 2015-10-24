package com.example.george.guessthepicture;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TimeUpFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Handler handler;
    private Runnable runnable;

    public TimeUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                fragmentDone();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_up, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        GameActivity gameActivity = (GameActivity) getActivity();
        gameActivity.soundPool.play(gameActivity.soundIDs[4], 1, 1, 1, 0, 1);
        handler.postDelayed(runnable, 2000);
    }

    public void fragmentDone() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
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
        void onFragmentInteraction();
    }

}
