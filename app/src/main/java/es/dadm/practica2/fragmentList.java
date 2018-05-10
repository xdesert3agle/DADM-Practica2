package es.dadm.practica2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.adapters.ListAdapter;

public class fragmentList extends Fragment {
    @BindView(R.id.rvBills)RecyclerView mRecycler;
    ArrayList<Category> mCategoryList;

    private static final String BUNDLE_BILL_LIST = "Ticket list";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bills_list_mode, container, false);
        ButterKnife.bind(this, view);

        mCategoryList = getArguments().getParcelableArrayList(BUNDLE_BILL_LIST);

        // TODO: Imprimir por orden alfabético de categorías.
        // TODO: Cambiar el adapter para que imprima todo el array de categorías por orden alfabético
        mRecycler.setAdapter(new ListAdapter((ArrayList) mCategoryList.get(0).getTicketList(), getActivity()));
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}
