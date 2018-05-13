package es.dadm.practica2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.adapters.CardAdapter;
import es.dadm.practica2.adapters.ListAdapter;

public class fragmentList extends Fragment {
    @BindView(R.id.rvTickets) RecyclerView mRecycler;
    ListAdapter mAdapter;
    List<Ticket> mTicketList;
    TicketSQLiteHelper mTicketDB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets_list_mode, container, false);
        ButterKnife.bind(this, view);

        mTicketDB = TicketSQLiteHelper.getInstance();
        mTicketList = mTicketDB.getTicketsFromBD();

        mAdapter = new ListAdapter(mTicketList, getActivity());
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshTicketList();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && mTicketDB != null) {
            refreshTicketList();
        }
    }

    public void refreshTicketList(){
        mTicketList = mTicketDB.getTicketsFromBD();
        mAdapter.setContent(mTicketList);
    }
}