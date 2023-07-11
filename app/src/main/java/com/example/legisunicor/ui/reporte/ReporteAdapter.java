package com.example.legisunicor.ui.reporte;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.legisunicor.R;
import com.example.legisunicor.db.models.Reporte;

import java.util.List;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.CustomViewHolder> {
    private List<Reporte> reporteList;

    private OnItemClickListener onItemClickListener;


    public ReporteAdapter(List<Reporte> dataList) {
        this.reporteList = dataList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reportes, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Reporte reporte = reporteList.get(position);
        holder.bindData(reporte);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return reporteList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView periodTextView;
        private TextView descriptionTextView;
        private CheckBox selectionCheckBox;
        private LinearLayout linearLayout;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            periodTextView = itemView.findViewById(R.id.txt_period);
            descriptionTextView = itemView.findViewById(R.id.txt_description);
            linearLayout = itemView.findViewById(R.id.linear_layout);

        }

        public void bindData(Reporte reporte) {
            periodTextView.setText(reporte.getPeriodo());
            descriptionTextView.setText(reporte.getDescripcion());

        }
    }

    public void setDataList(List<Reporte> reporteList) {
        this.reporteList = reporteList;
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


}