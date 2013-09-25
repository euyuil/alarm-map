package com.euyuil.alarmmap;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlarmListFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new SimpleAdapter(getActivity(), getData(), R.layout.list_item_alarm,
                new String[]{"title"}, new int[]{R.id.textView}));
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(new HashMap<String, Object>() {{ this.put("title", "haha"); }});
        list.add(new HashMap<String, Object>() {{ this.put("title", "hbhb"); }});
        list.add(new HashMap<String, Object>() {{ this.put("title", "hchc"); }});
        return list;
    }
}
