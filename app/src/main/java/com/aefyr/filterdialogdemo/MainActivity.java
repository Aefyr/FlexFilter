package com.aefyr.filterdialogdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.aefyr.flexfilter.builtin.DefaultFilterConfigViewHolderFactory;
import com.aefyr.flexfilter.builtin.filter.singlechoice.SingleChoiceFilterConfig;
import com.aefyr.flexfilter.builtin.filter.sort.SortFilterConfig;
import com.aefyr.flexfilter.config.core.ComplexFilterConfig;
import com.aefyr.flexfilter.config.core.FilterConfig;
import com.aefyr.flexfilter.ui.FilterDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FilterDialog.OnApplyConfigListener {

    private ComplexFilterConfig mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<FilterConfig> filters = new ArrayList<>();
        filters.add(new SortFilterConfig("bb", "Sort")
                .addOption("option 1", "Sort 1")
                .addOption("option 2", "Sort 2")
                .addOption("option 3", "Sort 3")
                .addOption("option 4", "Sort 4")
                .addOption("option 5", "Sort 5")
                .addOption("option 6", "Sort 6")
                .addOption("option 7", "Big Sort")
                .addOption("option 8", "Huge Sort")
                .addOption("option 9", "Sort Sort"));

        filters.add(new SingleChoiceFilterConfig("aa", "Big Single Option")
                .addOption("option 0", "Doesn't matter")
                .addOption("option 1", "Yes")
                .addOption("option 2", "No")
                .addOption("option 3", "Maybe")
                .addOption("option 4", "You tell me")
                .addOption("option 5", "Idk")
        );

        for (int i = 0; i < 100; i++) {
            filters.add(new SingleChoiceFilterConfig("bb" + i, "Single Option " + i)
                    .addOption("option 0", "Doesn't matter")
                    .addOption("option 1", "Yes")
                    .addOption("option 2", "No"));
        }


        mConfig = new ComplexFilterConfig(filters);

        findViewById(R.id.tv_aaaa).setOnClickListener((v) -> {
            FilterDialog.newInstance("Select filters", mConfig, DefaultFilterConfigViewHolderFactory.class).show(getSupportFragmentManager(), null);
        });
    }

    @Override
    public void onApplyConfig(ComplexFilterConfig config) {
        String a = "a";
        mConfig = config;
    }
}
