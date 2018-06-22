package com.whaddyalove.activities;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.whaddyalove.R;
import com.whaddyalove.bean.CommentsBean;
import com.whaddyalove.bean.LikesBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LikesActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private AQuery aQuery;
    private RecyclerView recyclerView;
    private List<LikesBean> likesList = new ArrayList<>();
    private LikesAdapter likesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        aQuery = new AQuery(this);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        TextView title = (TextView) toolBar.findViewById(R.id.title);
        title.setText("Loves");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        upArrow.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        likesList = (List<LikesBean>) getIntent().getSerializableExtra("likes_list");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        likesAdapter = new LikesAdapter(LikesActivity.this, likesList);
        recyclerView.setAdapter(likesAdapter);
    }

    public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.MyViewHolders> {

        private List<LikesBean> likesList;
        private Context context;

        public LikesAdapter(Context context, List<LikesBean> itemList) {
            this.likesList = itemList;
            this.context = context;
        }

        @Override
        public LikesAdapter.MyViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_likes, null);
            LikesAdapter.MyViewHolders myViewHolders = new LikesAdapter.MyViewHolders(layoutView);
            return myViewHolders;
        }

        @Override
        public void onBindViewHolder(LikesAdapter.MyViewHolders holder, int position) {
//            aQuery.id(holder.profile_img).image(likesList.get(position).getUserImg(), true, true, 0, 0, null, AQuery.FADE_IN);

            if(!TextUtils.isEmpty(likesList.get(position).getUserImg())){
                aQuery.id(holder.profile_img).image(likesList.get(position).getUserImg(), true, true, 0, 0, null, AQuery.FADE_IN);

            }else{
                holder.profile_img.setImageResource(R.drawable.logo_with_whitebg);
            }

            holder.userName.setText(likesList.get(position).getFirstname() + " " + likesList.get(position).getLastname());
            holder.date.setText(likesList.get(position).getDate());
        }

        @Override
        public int getItemCount() {
            return this.likesList.size();
        }

        public class MyViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView profile_img;
            public TextView userName, date;

            public MyViewHolders(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                profile_img = (ImageView) itemView.findViewById(R.id.profile_img);
                userName = (TextView) itemView.findViewById(R.id.userName);
                date = (TextView) itemView.findViewById(R.id.comment_date);

            }

            @Override
            public void onClick(View view) {
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
