package bkacad.com.vn.adapters;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import bkacad.com.vn.R;
import bkacad.com.vn.domains.Song;

public class AdapterSong extends RecyclerView.Adapter<AdapterSong.MyViewHolder> {
    android.content.Context context;
    android.view.LayoutInflater layoutInflater;
    java.util.ArrayList<Song> songs = new java.util.ArrayList<>();
    public static java.lang.String TAG = "EmployeeAdapter";
    public MyItemClickListener myItemClickListener;

    public AdapterSong(android.content.Context context, java.util.ArrayList<Song> songs) {
        this.context = context;
        this.layoutInflater = (android.view.LayoutInflater) context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
        this.songs = songs;
    }


    @java.lang.Override
    public void onBindViewHolder(@androidx.annotation.NonNull MyViewHolder holder,
                                 int position) {
        Song song = songs.get(position);
        holder.tvName.setText(song.getName());
    }

    @androidx.annotation.NonNull
    @java.lang.Override
    public MyViewHolder onCreateViewHolder(@androidx.annotation.NonNull android.view.ViewGroup parent, int viewType) {
        android.view.View v = layoutInflater.inflate(R.layout.item_music, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @java.lang.Override
    public int getItemCount() {
        return this.songs.size();
    }

    public class MyViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        android.widget.TextView tvName;

        public MyViewHolder(@androidx.annotation.NonNull android.view.View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.tvName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myItemClickListener.onItemClick(songs.get(getPosition()), getPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public interface MyItemClickListener {
        void onItemClick(Song song, int activeSongIndex);
    }
}

