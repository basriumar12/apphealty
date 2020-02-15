package com.tenilodev.healthyfinder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tenilodev.healthyfinder.model.Tempat;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by aisatriani on 20/08/15.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.TempatViewHolder> {

    private List<Tempat> tempats;
    private int idkategori;
    onItemClickListener mItemClickListener;

    public RVAdapter(List<Tempat> listTempat, int idkategori){
        this.tempats = listTempat;
        this.idkategori = idkategori;
    }

    @Override
    public TempatViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        TempatViewHolder tvh = new TempatViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(TempatViewHolder tempatViewHolder, int i) {

        tempatViewHolder.personName.setText(tempats.get(i).getNama_tempat());
        tempatViewHolder.personAge.setText(tempats.get(i).getBuka());
        double kmdouble = tempats.get(i).getJarak() * 0.001;
        String km = new DecimalFormat("#.###").format(kmdouble);
        tempatViewHolder.personDistance.setText(km + " km");

        if(idkategori == 1) {
            tempatViewHolder.personPhoto.setImageResource(R.drawable.doctor);
            tempatViewHolder.tv_keterangan.setText(tempats.get(i).getKeterangan());
        }
        if(idkategori == 2) {
            tempatViewHolder.personPhoto.setImageResource(R.drawable.apotek);
            tempatViewHolder.tv_keterangan.setVisibility(View.GONE);
        }
        if(idkategori == 3){
            tempatViewHolder.personPhoto.setImageResource(R.drawable.ambulance);
            tempatViewHolder.tv_keterangan.setVisibility(View.GONE);
        }

        if(idkategori == 4){
            tempatViewHolder.personPhoto.setImageResource(R.drawable.clinic);
            tempatViewHolder.tv_keterangan.setVisibility(View.GONE);
        }

        if(idkategori == 5) {
            tempatViewHolder.personPhoto.setImageResource(R.drawable.hospital);
            tempatViewHolder.tv_keterangan.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return tempats.size();
    }

    @Override
    public long getItemId(int position) {
        return tempats.get(position).getId_tempat();
    }

    public class TempatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_keterangan;
        CardView cv;
        TextView personName;
        TextView personAge;
        TextView personDistance;
        ImageView personPhoto;


        public TempatViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personDistance = (TextView)itemView.findViewById(R.id.person_distance);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            tv_keterangan = (TextView)itemView.findViewById(R.id.tv_keterangan);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getPosition());
        }
    }

    public interface onItemClickListener{
        public void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(final onItemClickListener mItemClick){
        this.mItemClickListener = mItemClick;
    }
}
