package fq.fuqueue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllProductListAdapter extends RecyclerView.Adapter{
    ArrayList<Product> items;
    Context context;
    OfflineProductAdapter offlinelistadapter;
    public AllProductListAdapter(ArrayList<Product> items,OfflineProductAdapter offlineadapter, Context context){
        this.items = items;
        this.context = context;
        this.offlinelistadapter = offlineadapter;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.activity_offline_row_product, parent, false);
        return new ItemHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        String name = (String) items.get(position).name;
        String price = (String) "" + (items.get(position).price);
        String description = items.get(position).description;
        String quantity = (String) "" + (items.get(position).quantity);
        ((ItemHolder)holder).product_name.setText(name);
        ((ItemHolder)holder).product_price.setText(price);
        ((ItemHolder)holder).product_description.setText(description);
        ((ItemHolder)holder).product_quantity.setText(quantity);
        ((ItemHolder)holder).button_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Product new_product = new Product(items.get(position));
                ArrayList<Product> productList = ProductListManager.getOfflineListProducts(context);
                ProductListManager.addProductToList(productList,new_product);
                ProductListManager.storeOfflineListProducts(productList,context);
                offlinelistadapter.load_saved_values(context);
                offlinelistadapter.notify_data_changed();
            }
        });
        Picasso.with(context).load(R.drawable.a).resize(88,88).into(((ItemHolder) holder).imageViewThumbnail);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void notify_data_changed(){
        this.notifyDataSetChanged();
    }
    private class ItemHolder extends RecyclerView.ViewHolder{
        TextView product_name, product_price, product_description,product_quantity;
        android.widget.Button button_add;
        ImageView imageViewThumbnail;
        public ItemHolder(View itemView){
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_description = itemView.findViewById(R.id.product_description);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            product_quantity = itemView.findViewById(R.id.product_quantity);
            button_add = itemView.findViewById(R.id.button_add);
        }
    }
}
