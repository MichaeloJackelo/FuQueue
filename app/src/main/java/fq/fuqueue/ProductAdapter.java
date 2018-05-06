package fq.fuqueue;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter{
    ArrayList<Product> items;
    Context context;
    public ProductAdapter(ArrayList<Product> items, Context context){
        this.items = items;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.custom_row_item, parent, false);
        return new ItemHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String text = (String) items.get(position).name;
        ((ItemHolder)holder).textView.setText(text);
        Picasso.with(context).load(R.drawable.a).resize(200,200).into(((ItemHolder) holder).imageViewThumbnail);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    private class ItemHolder extends RecyclerView.ViewHolder{
        TextView textView, text_view_title, text_view_description;
        ImageView imageViewThumbnail;
        public ItemHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            text_view_title = itemView.findViewById(R.id.text_view_title);
            text_view_description = itemView.findViewById(R.id.text_view_description);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
        }
    }
}
