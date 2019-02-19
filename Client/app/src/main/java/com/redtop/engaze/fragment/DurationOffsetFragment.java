package com.redtop.engaze.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.redtop.engaze.R;
import com.redtop.engaze.entity.Duration;
import com.redtop.engaze.utils.AppUtility;

import java.util.ArrayList;

public class DurationOffsetFragment extends DialogFragment {

    private OnFragmentInteractionListener mListener;
    private ArrayList<TextView> periods;
    private Duration duration = null;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDurationOffsetFragmentInteraction(Duration duration);
    }

    public DurationOffsetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_setduration, container, false);
        duration = (Duration) getArguments().getParcelable("duration");

        Button save = (Button) rootView.findViewById(R.id.save_event_duration);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText intervalEditText = (EditText) rootView.findViewById(R.id.DurationValue);

                if (!intervalEditText.getText().toString().isEmpty()) {
                    try {
                        int userInput = Integer.parseInt(intervalEditText.getText().toString());
                        duration.setTimeInterval(userInput);
                        if (AppUtility.validateDurationInput(duration, getActivity())) {
                            AppUtility.hideKeyboard(v, getActivity());
                            mListener.onDurationOffsetFragmentInteraction(duration);
                            dismiss();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getActivity(),
                                getResources().getString(R.string.message_createEvent_durationMaxAlert),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(),
                            getResources().getString(R.string.event_invalid_input_message),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        EditText text = (EditText) rootView.findViewById(R.id.DurationValue);
        text.setText(Integer.toString(duration.getTimeInterval()));

        periods = new ArrayList<TextView>();
        TextView period = null;

        period = (TextView) rootView.findViewById(R.id.DurationMinutes);
        setDefaultDurationPeriod(period);
        periods.add(period);

        period = (TextView) rootView.findViewById(R.id.DurationHours);
        setDefaultDurationPeriod(period);
        periods.add(period);

        for (int i = 0; i < periods.size(); i++) {
            TextView pr = periods.get(i);

            pr.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    for (int i = 0; i < periods.size(); i++) {
                        TextView dv = periods.get(i);
                        dv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        String duration = dv.getText().toString();
                        dv.setTextColor(getResources().getColorStateList(R.color.primary_text));

                    }
                    TextView dur = ((TextView) v);
                    Drawable draw = getResources().getDrawable(R.drawable.primary_color_check);
                    dur.setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
                    dur.setTextColor(getResources().getColorStateList(R.color.primary));
                    duration.setPeriod(dur.getTag().toString());
                }
            });

        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DurationOffsetFragment.OnFragmentInteractionListener) {
            mListener = (DurationOffsetFragment.OnFragmentInteractionListener) context;
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

    private void setDefaultDurationPeriod(TextView period) {
        if (period.getTag().equals(duration.getPeriod())) {
            //period.setText(period.getText().toString().concat(getResources().getString(R.string.duration)));
            period.setTextColor(getResources().getColorStateList(R.color.primary));
            Drawable draw = getResources().getDrawable(R.drawable.primary_color_check);
            period.setCompoundDrawablesWithIntrinsicBounds(null, null, draw, null);
        }
    }
}
