package com.karepin.homework_020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_1 = "key-1";
    private static final String KEY_2 = "key-2";
    List<Map<String, String>> mapForContent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView list = findViewById(R.id.layout_list_view);

        prepareContent();
        final BaseAdapter baseAdapter = createAdapter(mapForContent);
        list.setAdapter(baseAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mapForContent.remove(position);
                baseAdapter.notifyDataSetChanged();
            }
        });
        final SwipeRefreshLayout refreshLayout = findViewById(R.id.swipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mapForContent.clear();
                prepareContent();
                baseAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void prepareContent() {
        try {
            prepareContentForPref();
        } catch (Exception e) {
            e.printStackTrace();
            prepareContentForAssets();
            SharedPreferences preferences = getSharedPreferences("myValue", MODE_PRIVATE);
            preferences.edit().putString("values", getString(R.string.large_text)).apply();
        }
    }

    private void prepareContentForPref() throws Exception {
        SharedPreferences preferences = getSharedPreferences("myValue", MODE_PRIVATE);
        String stringPref = preferences.getString("value", "");

        String[] strings;
        if (!stringPref.isEmpty()) {
            strings = stringPref.split("\n\n");
        } else {
            throw new Exception("Шарпреференс не имеет значений");
        }
        for (String str : strings) {
            Map<String, String> map = new HashMap<>();
            map.put(KEY_1, String.valueOf(str.length()));
            map.put(KEY_2, str);
            mapForContent.add(map);
        }
    }

    private BaseAdapter createAdapter(List<Map<String, String>> myContent) {
        String[] from = {KEY_1, KEY_2};
        int[] to = {R.id.textView, R.id.textView2};
        return new SimpleAdapter(this, myContent, R.layout.content_list_view, from, to);
    }


    private void prepareContentForAssets() {
        String[] text = getString(R.string.large_text).split("\n\n");
        for (String str : text) {
            Map<String, String> map = new HashMap<>();
            map.put(KEY_1, String.valueOf(str.length()));
            map.put(KEY_2, str);
            mapForContent.add(map);
        }
    }
}
