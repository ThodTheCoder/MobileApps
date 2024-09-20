package com.mirea.elizarovnm.mireaproject;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;

import com.mirea.elizarovnm.mireaproject.databinding.FragmentVoiceRecorderBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VoiceRecorderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VoiceRecorderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private enum State{
        IDLE,
        RECORD,
        PLAYING
    }
    private	static final int REQUEST_CODE_PERMISSION = 100;
    private	boolean	isWork = false;
    private FragmentVoiceRecorderBinding binding = null;
    private String recordFilePath = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private State state = State.IDLE;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VoiceRecorderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VoiceRecorderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VoiceRecorderFragment newInstance(String param1, String param2) {
        VoiceRecorderFragment fragment = new VoiceRecorderFragment();
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
        binding = FragmentVoiceRecorderBinding.inflate(inflater, container, false);
        binding.recordVoiceButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OnRecordButtonClicked();
                    }
                }
        );
        binding.playVoiceButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OnPlayButtonClicked();
                    }
                }
        );

        recordFilePath = (new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecordtest.3gp")).getAbsolutePath();
        int	audioPermissionStatus = ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.RECORD_AUDIO);
        int	storagePermissionStatus	= ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (audioPermissionStatus == PackageManager.PERMISSION_GRANTED
                && storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {android.Manifest.permission.RECORD_AUDIO,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }

        return binding.getRoot();
    }
    private void OnRecordButtonClicked() {
        switch (state) {
            case IDLE: {
                StartRecord();
                break;
            }
            case RECORD: {
                StopRecord();
                break;
            }
            case PLAYING: { break; }
            default: { break; }
        }
    }
    private void OnPlayButtonClicked() {
        switch (state) {
            case IDLE: {
                StartPlaying();
                break;
            }
            case PLAYING: {
                StopPlaying();
                break;
            }
            case RECORD: { break; }
            default: { break; }
        }
    }


    public void StartPlaying() {
        if(state == State.PLAYING) return;
        state = State.PLAYING;
        binding.recordVoiceButton.setEnabled(false);
        binding.playVoiceButton.setEnabled(true);

        try	{
            player = new MediaPlayer();
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e("Work", "prepare() failed");
        }
    }
    public void StopPlaying() {
        if(state == State.IDLE) return;
        state = State.IDLE;
        binding.recordVoiceButton.setEnabled(true);
        binding.playVoiceButton.setEnabled(true);

        player.release();
        player = null;
    }
    public void StartRecord() {
        if(state == State.RECORD) return;
        state = State.RECORD;
        binding.recordVoiceButton.setEnabled(true);
        binding.playVoiceButton.setEnabled(false);

        try	{
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(recordFilePath);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.prepare();
            recorder.start();
        } catch (IOException e)	{
            Log.e("Work",	"prepare()	failed");
        }
    }
    public void StopRecord() {
        if(state == State.IDLE) return;
        state = State.IDLE;
        binding.recordVoiceButton.setEnabled(true);
        binding.playVoiceButton.setEnabled(true);

        recorder.stop();
        recorder.release();
        recorder = null;
    }
}