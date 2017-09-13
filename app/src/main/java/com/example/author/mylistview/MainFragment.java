package com.example.author.mylistview;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainFragment extends Fragment {

    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(R.id.list_view);

        FloatingActionButton addBtn = (FloatingActionButton) view.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailFragment fragment = new DetailFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                transaction.addToBackStack(null).
                        replace(R.id.main_fragment, fragment)
                        .commit();
            }
        });

        //db
        MyDBHelper myDBHelper = new MyDBHelper(getActivity());
        SQLiteDatabase db = myDBHelper.getWritableDatabase();

        //select
        final Cursor cursor = db.rawQuery("select * from reminder_record", null);

        //adapter
        String[] from = {"title", "body"};
        int[] to = {R.id.title_view, R.id.body_view};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.listview_item, cursor, from, to, 0);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailFragment fragment = new DetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", cursor.getString(cursor.getColumnIndex("title")));
                bundle.putString("body", cursor.getString(cursor.getColumnIndex("body")));
                bundle.putString("id", cursor.getString(cursor.getColumnIndex("_id")));
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

}

