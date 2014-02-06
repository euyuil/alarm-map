package com.euyuil.alarmmap;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.euyuil.alarmmap.provider.AlarmContract;

import de.timroes.android.listview.EnhancedListView;

public class AlarmListFragment extends ListFragment implements LoaderCallbacks<Cursor> {

    EnhancedListView listView;
    AlarmListAdapter listAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        listView = (EnhancedListView) getListView();

        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditAlarmLocationActivity.class);
                intent.putExtra("alarm", id);
                startActivity(intent);
            }
        });

        listView.setLongClickable(true);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Alarm alarm = Alarm.findById(id);
                if (alarm != null)
                    alarm.delete();
                return true;
            }
        });

        listView.setDismissCallback(new EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView enhancedListView, int i) {
                long id = listAdapter.getItemId(i);
                final Alarm alarm = Alarm.findById(id);
                if (alarm != null)
                    alarm.delete();
                return new EnhancedListView.Undoable() {
                    @Override
                    public void undo() {
                        if (alarm != null)
                            alarm.insert();
                    }
                };
            }
        });

        listView.enableSwipeToDismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        listAdapter = new AlarmListAdapter(getActivity());

        setListAdapter(listAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                Uri.parse("content://com.euyuil.alarmmap.provider/alarm"),
                AlarmContract.PROJECTION_STAR, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        listAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        listAdapter.swapCursor(null);
    }
}
