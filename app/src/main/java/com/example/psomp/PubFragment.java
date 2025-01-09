package com.example.psomp;// PubFragment.java
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class PubFragment extends Fragment {

    private static final String ARG_PUB_NAME = "pubName";

    public static PubFragment newInstance(String pubName) {
        PubFragment fragment = new PubFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PUB_NAME, pubName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pub, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String pubName = getArguments() != null ? getArguments().getString(ARG_PUB_NAME) : "Pub";

        TextView pubNameTextView = view.findViewById(R.id.pubNameTextView);
        pubNameTextView.setText(pubName);

        ;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}
