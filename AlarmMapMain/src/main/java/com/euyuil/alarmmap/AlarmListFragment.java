package com.euyuil.alarmmap;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.euyuil.alarmmap.AlarmContract.AlarmEntry;

public class AlarmListFragment extends ListFragment implements LoaderCallbacks<Cursor> {

    SimpleCursorAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item_alarm, null,
                new String[] {
                        AlarmContract.AlarmEntry.COLUMN_NAME_ALARM_TITLE
                }, new int[] {
                        R.id.textView
                }, 0);

        setListAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                Uri.parse("content://com.euyuil.alarmmap.provider/alarm"),
                AlarmEntry.PROJECTION_ALARM_DETAILS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }
}
