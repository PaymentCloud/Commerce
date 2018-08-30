package paymentcloud.creaj.sv.com.customerspcloud;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Historial> historialList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, credit;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            credit = (TextView) view.findViewById(R.id.credit);
            year = (TextView) view.findViewById(R.id.date);
        }
    }


    public Adapter(List<Historial> historialList) {
        this.historialList = historialList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Historial historial = historialList.get(position);

        holder.title.setText(historial.getCustomDescription().toString());
        if (historial.getOperation().equals("A")){
            holder.credit.setTextColor(Color.parseColor("#007f00"));
            holder.credit.setText("+"+historial.getAmount().toString());
        }else{
            holder.credit.setTextColor(Color.parseColor("#ff0000"));
            holder.credit.setText("-"+historial.getAmount().toString());
        }


        holder.year.setText(historial.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return historialList.size();
    }
}
