package com.xlip.pegasusflightchecker.fragment;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xlip.pegasusflightchecker.R;
import com.xlip.pegasusflightchecker.storage.MyData;
import com.xlip.pegasusflightchecker.storage.MyStorage;
import com.xlip.pegasusflightchecker.storage.SearchObject;

public class SelectSavedSearchFragment extends DialogFragment {
    MyData myData;
    SelectSavedSearchFragmentCallback callback;
    ScrollView mainView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(R.style.Theme_Shrine , R.style.Theme_MaterialComponents_Dialog);
        this.myData = MyStorage.getData(getActivity());


    }

    private void initViewWithData(){
        mainView.removeAllViews();

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setMinimumWidth(500);
        linearLayout.setPadding(40,40,40,40);
        if(myData == null || myData.getSearchObjects() == null || myData.getSearchObjects().size() == 0) {
            TextView textView = new TextView(getActivity());
            textView.setText("No data found");
            textView.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);
        }else {
            myData.getSearchObjects().forEach(search -> {
                LinearLayout row = new LinearLayout(getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(Gravity.START);

                linearLayout.setOrientation(LinearLayout.VERTICAL);

                Button b = new Button(getActivity());

                b.setGravity(Gravity.CENTER);
                b.setText(search.getTitle());
                b.setOnClickListener((View view) -> {
                    this.dismiss();
                    callback.onSelect(search);
                });
                row.addView(b);

                Button deleteButton = new Button(getActivity());
                b.setGravity(Gravity.CENTER);

                deleteButton.setTextColor(Color.WHITE);
                deleteButton.setText("X");
                deleteButton.setOnClickListener((View view) -> {
                    myData.getSearchObjects().removeIf(n -> (n.getId() == search.getId()));
                    MyStorage.saveData(getContext(), myData);
                    initViewWithData();
                });
                row.addView(deleteButton);



                linearLayout.addView(row);

            });
        }

        mainView.addView(linearLayout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = new ScrollView(getContext());

        initViewWithData();
        return mainView;
    }

    public SelectSavedSearchFragmentCallback getCallback() {
        return callback;
    }

    public void setCallback(SelectSavedSearchFragmentCallback callback) {
        this.callback = callback;
    }

    public interface SelectSavedSearchFragmentCallback{
        void onSelect(SearchObject searchObject);

    }
}
