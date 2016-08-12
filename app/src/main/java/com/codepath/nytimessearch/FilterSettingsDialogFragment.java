package com.codepath.nytimessearch;


import android.app.DatePickerDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.parceler.Parcels;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterSettingsDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.etBeginDate) EditText etBeginDate;
    @BindView(R.id.spinnerSortOrder) Spinner spinnerSortOrder;
    @BindView(R.id.cbArts) CheckBox cbArts;
    @BindView(R.id.cbFashionStyle) CheckBox cbFashionStyle;
    @BindView(R.id.cbSports) CheckBox cbSports;
    @BindView(R.id.btnSearch) Button btnSearch;

    private FilterSettingsListener listener;
    private FilterSettings filterSettings;

    public static String ARG_FILTER_SETTINGS = "arg_filter_settings";

    public FilterSettingsDialogFragment() {

    }

    public static FilterSettingsDialogFragment newInstance(FilterSettings filterSettings) {
        FilterSettingsDialogFragment fragment = new FilterSettingsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_FILTER_SETTINGS, Parcels.wrap(filterSettings));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_filters, container);
        ButterKnife.bind(this, view);

        filterSettings = Parcels.unwrap(getArguments().getParcelable(ARG_FILTER_SETTINGS));
        /*
        if (getArguments().getParcelable(ARG_FILTER_SETTINGS) != null) {
            filterSettings = Parcels.unwrap(getArguments().getParcelable(ARG_FILTER_SETTINGS));
        } else {
            filterSettings = new FilterSettings();
        }
        */
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerSortOrder.getAdapter();

        etBeginDate.setText(filterSettings.getBeginDate());
        spinnerSortOrder.setSelection(adapter.getPosition(filterSettings.getSortOrder()));
        cbArts.setChecked(filterSettings.isNewsDeskArt());
        cbFashionStyle.setChecked(filterSettings.isNewsDeskFashionStyle());
        cbSports.setChecked(filterSettings.isNewsDeskSports());

        etBeginDate.setOnClickListener((v) -> showDatePickerDialog());
        btnSearch.setOnClickListener((v) -> {
            setFilterSettings();
            listener.onSubmit(filterSettings);
            dismiss();
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(R.string.title_search_filters);
        etBeginDate.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void showDatePickerDialog() {
        BeginDateFragment beginDateFragment = new BeginDateFragment();
        beginDateFragment.setTargetFragment(FilterSettingsDialogFragment.this, 300);
        beginDateFragment.show(getFragmentManager(), "fragment_begin_date");
    }

    private void setFilterSettings() {
        filterSettings.setBeginDate(etBeginDate.getText().toString());
        filterSettings.setSortOrder(spinnerSortOrder.getSelectedItem().toString());
        filterSettings.setNewsDeskArt(cbArts.isChecked());
        filterSettings.setNewsDeskFashionStyle(cbFashionStyle.isChecked());
        filterSettings.setNewsDeskSports(cbSports.isChecked());
    }

    public void setSearchFiltersListener(FilterSettingsListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String beginDate = String.format(Locale.ENGLISH, "%1$d%2$02d%3$02d", year, month, day);
        etBeginDate.setText(beginDate);
    }

    public interface FilterSettingsListener {
        void onSubmit(FilterSettings filterSettings);
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        super.onResume();
    }
}
