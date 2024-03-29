package es.dadm.practica2.Screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import es.dadm.practica2.Util.ImgUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.Adapters.TileAdapter;
import es.dadm.practica2.Interfaces.ElementActions;
import es.dadm.practica2.R;
import es.dadm.practica2.Objects.Ticket;
import es.dadm.practica2.Objects.TicketDB;

public class FragmentTiles extends Fragment {
    @BindView(R.id.rvTickets) RecyclerView mRecycler;

    private TileAdapter mAdapter;
    private List<Ticket> mTicketList;
    private TicketDB mTicketDB;
    private int mSelTicketPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets_tile_mode, container, false);
        ButterKnife.bind(this, view);
        registerForContextMenu(mRecycler);

        // Se recogen los tickets de la base de datos
        mTicketDB = TicketDB.getInstance();
        mTicketList = mTicketDB.getTicketsFromBD();

        // Adapter para el RecyclerView
        mAdapter = new TileAdapter(mTicketList, getActivity(), new ElementActions() {
            @Override
            public void onItemClicked(int position) {
                startActivity(new Intent(getActivity(), AddEditTicket.class).putExtra(FragmentList.TAG_TICKET_POSITION, mTicketList.get(position).getId()));
            }

            @Override
            public void onCreateContextMenu(View view, ContextMenu menu, int position) {
                mSelTicketPosition = position;

                MenuInflater inflater = new MenuInflater(getActivity());
                inflater.inflate(R.menu.long_press_menu, menu);
            }
        });

        // Se asigna el adapter al Recycler para printear los tickets
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshTicketList();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            switch (item.getItemId()){
                case R.id.lpDeleteElement: // Opción eliminar ticket
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.MSG_TICKET_DELETE_CONFIRM)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteSelectedTicket();
                                    refreshTicketList();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                    break;
            }

            return true;
        }

        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mTicketDB != null) {
            refreshTicketList();
        }
    }

    public void refreshTicketList(){
        mTicketList = mTicketDB.getTicketsFromBD();
        mAdapter.setContent(mTicketList);
        mAdapter.notifyDataSetChanged();
    }

    public void deleteSelectedTicket(){
        // Se recupera el ticket sobre el que el usuario ha hecho la pulsación larga
        Ticket selectedTicket = mTicketList.get(mSelTicketPosition);

        ImgUtil.deleteImage(selectedTicket.getImgFilename(), getActivity()); // Se borra tanto la imagen del ticket de la memoria externa del teléfono...
        mTicketDB.deleteTicket(selectedTicket); // ...como el ticket de la base de datos

        Toast.makeText(getActivity(), String.format(getString(R.string.MSG_DELETED_TICKET), selectedTicket.getTitle()), Toast.LENGTH_SHORT).show();
        setToolbarTicketCount(); // Se refresca el contador de tickets de la toolbar
    }

    private void setToolbarTicketCount(){
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(String.format(getResources().getString(R.string.TITLE_TICKET_TAB_CONTAINER), mTicketDB.getTicketCount()));
    }
}
