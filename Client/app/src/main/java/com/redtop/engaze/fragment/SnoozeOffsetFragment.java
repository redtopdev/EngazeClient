package com.redtop.engaze.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.redtop.engaze.R;
import com.redtop.engaze.entity.Duration;

public class SnoozeOffsetFragment extends DialogFragment {
    private Duration snoozeDuration = null;
    private EditText text;
    private Context mContext;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSnoozeOffsetFragmentInteraction(Duration duration);
    }

    /**
     * Called when the fragment is first created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_snooze, container, false);
        snoozeDuration = new Duration(30, "minute", true);
        mContext = getActivity();
        text = (EditText) rootView.findViewById(R.id.SnoozeValue);
        text.setText(Integer.toString(snoozeDuration.getTimeInterval()));

        Button save = (Button) rootView.findViewById(R.id.save_event_Snooze);
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!text.getText().toString().isEmpty()) {
                    try {
                        int userInput = Integer.parseInt(text.getText().toString());
                        //if(AppUtility.validateDurationInput(snoozeDuration, getBaseContext())){
                        if (userInput >= getResources().getInteger(R.integer.runningevent_min_extend_minutes) && userInput <= getResources().getInteger(R.integer.runningevent_max_extend_minutes)) {
                            snoozeDuration.setTimeInterval(userInput);
                            mListener.onSnoozeOffsetFragmentInteraction(snoozeDuration);
                            dismiss();

                        } else {
                            Toast.makeText(mContext,
                                    getResources().getString(R.string.message_runningEvent_extendDurationValidation),
                                    Toast.LENGTH_LONG).show();

                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(mContext,
                                getResources().getString(R.string.message_runningEvent_extendDurationValidation),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mContext,
                            getResources().getString(R.string.event_invalid_input_message),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        Button cancel = (Button) rootView.findViewById(R.id.cancel_event_Snooze);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SnoozeOffsetFragment.OnFragmentInteractionListener) {
            mListener = (SnoozeOffsetFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
