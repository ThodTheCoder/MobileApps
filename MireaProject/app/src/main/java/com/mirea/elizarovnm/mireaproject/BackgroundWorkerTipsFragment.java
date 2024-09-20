package com.mirea.elizarovnm.mireaproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;

import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BackgroundWorkerTipsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BackgroundWorkerTipsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BackgroundWorkerTipsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BackgroundWorkerTipsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BackgroundWorkerTipsFragment newInstance(String param1, String param2) {
        BackgroundWorkerTipsFragment fragment = new BackgroundWorkerTipsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_background_worker_tips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView TipTextView = view.findViewById(R.id.tipTextView);

        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(WeatherTips.class)
                        .build();
        WorkManager
                .getInstance()
                .enqueue(uploadWorkRequest);

        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(uploadWorkRequest.getId())
                .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            String randomTip = workInfo.getOutputData().getString("Tip");
                            TipTextView.setText(randomTip);
                        }
                    }
                });

    }
}