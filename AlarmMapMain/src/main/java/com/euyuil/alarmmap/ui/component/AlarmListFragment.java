package com.euyuil.alarmmap.ui.component;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

import com.euyuil.alarmmap.AlarmUtils;
import com.euyuil.alarmmap.ui.EditAlarmActivity;
import com.euyuil.alarmmap.ui.EditLocationActivity;
import com.euyuil.alarmmap.R;
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
                startActivity(new Intent(getActivity(), EditAlarmActivity.class)
                        .setData(AlarmUtils.getUri(id)));
            }
        });

        listView.setLongClickable(true);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Uri uri = AlarmUtils.getUri(id);
//                int result = AlarmListFragment.this.getActivity()
//                        .getContentResolver().delete(uri, null, null);
//                assert result <= 1;
//                return result == 1;
                return true;
            }
        });

        listView.setDismissCallback(new EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView enhancedListView, int i) {
                long id = listAdapter.getItemId(i);
                Uri uri = AlarmUtils.getUri(id);
                Cursor cursor = AlarmListFragment.this.getActivity()
                        .getContentResolver().query(uri, null, null, null, null);
                if (cursor == null || !cursor.moveToFirst())
                    return null;
                final ContentValues alarm = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, alarm);
                int result = AlarmListFragment.this.getActivity()
                        .getContentResolver().delete(uri, null, null);
                assert result <= 1;
                if (result == 1) {
                    return new EnhancedListView.Undoable() {
                        @Override
                        public void undo() {
                            AlarmListFragment.this.getActivity().getContentResolver()
                                    .insert(AlarmContract.TABLE_CONTENT_URI, alarm);
                        }
                    };
                }
                return null;
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
