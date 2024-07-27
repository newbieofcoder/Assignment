package hoanglv.fpoly.assignment.Adapters;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hoanglv.fpoly.assignment.Interface.ProductInterface;
import hoanglv.fpoly.assignment.Models.Product;
import hoanglv.fpoly.assignment.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private ProductInterface productInterface;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public void setOnItemClickListener(ProductInterface productInterface) {
        this.productInterface = productInterface;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false);
        return new ProductViewHolder(view, productInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        final Product product = productList.get(position);
        holder.tv_name_product.setText(product.getTensp());
        holder.tv_price_product.setText(product.getGia() + " VND" + " - SL: " + product.getSoLuong());
        holder.img_product.setImageBitmap(product.getImg());
        holder.tv_edit.setOnClickListener(v -> productInterface.onEditClick(position));
        holder.tv_delete.setOnClickListener(v -> productInterface.onDeleteClick(position));
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name_product, tv_price_product, tv_edit, tv_delete;
        ImageView img_product;
        public ProductViewHolder(@NonNull View itemView, final ProductInterface productInterface) {
            super(itemView);
            tv_name_product = itemView.findViewById(R.id.tv_name_product);
            tv_price_product = itemView.findViewById(R.id.tv_price_product);
            tv_edit = itemView.findViewById(R.id.btn_edit);
            tv_delete = itemView.findViewById(R.id.btn_delete);
            img_product = itemView.findViewById(R.id.img_product);
            tv_edit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (productInterface != null && position != RecyclerView.NO_POSITION){
                    productInterface.onEditClick(position);
                }
            });
            tv_delete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (productInterface != null && position != RecyclerView.NO_POSITION){
                    productInterface.onDeleteClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
