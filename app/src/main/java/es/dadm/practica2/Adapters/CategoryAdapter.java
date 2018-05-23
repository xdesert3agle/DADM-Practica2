package es.dadm.practica2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dadm.practica2.Objects.Category;
import es.dadm.practica2.Util.ImgUtil;
import es.dadm.practica2.Interfaces.ElementActions;
import es.dadm.practica2.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.TileViewHolder>{
    private List<Category> mCategoryList;
    private Context mContext;
    private ElementActions mElementActions;

    public CategoryAdapter(List<Category> categoryList, Context context, ElementActions elementActions){
        this.mCategoryList = categoryList;
        this.mContext = context;
        this.mElementActions = elementActions;
    }

    class TileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.ivImg) ImageView ivImg;

        TileViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            mElementActions.onItemClicked(getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            mElementActions.onCreateContextMenu(view, contextMenu, getAdapterPosition());
        }
    }

    @Override
    public TileViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_tile_mode, viewGroup, false);

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int desiredWidth = displayMetrics.widthPixels / 2;

        v.getLayoutParams().width = desiredWidth;
        v.getLayoutParams().height = desiredWidth;

        return new TileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TileViewHolder holder, int position) {
        Category category = mCategoryList.get(position);

        holder.ivImg.setImageBitmap(ImgUtil.getImageAsBitmap(category.getImgFilename(), mContext));
        holder.tvTitle.setText(category.getTitle());
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }
}