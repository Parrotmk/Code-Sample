package com.whaddyalove.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.whaddyalove.R;
import com.whaddyalove.bean.CommentsBean;
import com.whaddyalove.bean.LikesBean;
import com.whaddyalove.common.UrlConstants;
import com.whaddyalove.helpers.ConversionHelper;
import com.whaddyalove.utils.DialogUtil;
import com.whaddyalove.utils.SessionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private AQuery aQuery;
    private RecyclerView recyclerView;
    private List<CommentsBean> commentsList = new ArrayList<>();
    private CommentsAdapter likesAdapter;
    private EditText etAddComment;
    private ImageView addComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        aQuery = new AQuery(this);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        TextView title = (TextView) toolBar.findViewById(R.id.title);
        title.setText("Comments");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        upArrow.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        etAddComment = (EditText) findViewById(R.id.et_add_comment);
        addComment = (ImageView) findViewById(R.id.add_comment_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        getCommentsList();


        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAddComment.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(CommentsActivity.this, "Please enter your comment first.", Toast.LENGTH_SHORT).show();
                } else {
                    addNewComment();
                }

            }
        });
    }

    private void getCommentsList() {
        if (DialogUtil.checkInternetConnection(CommentsActivity.this)) {
            String url = UrlConstants.BASE_URL + UrlConstants.GET_ALL_COMMENTS;
            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            Map params = new HashMap();
            params.put("post_id", getIntent().getStringExtra("post_id"));
            aQuery.progress(progress).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    if (json != null ) {
                        try {
                            if (json.getInt("responseCode") == 1) {
                                //JSONArray infoObj = json.getJSONArray("data");
                                commentsList = ConversionHelper.getAllCommentsList(json);
                                likesAdapter = new CommentsAdapter(CommentsActivity.this, commentsList);
                                recyclerView.setAdapter(likesAdapter);
                            } else {
                                Toast.makeText(CommentsActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(CommentsActivity.this, "Data not found.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            DialogUtil.showNoConnectionDialog(CommentsActivity.this);
        }
    }

    public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolders> {

        private List<CommentsBean> itemList;
        private Context context;

        public CommentsAdapter(Context context, List<CommentsBean> itemList) {
            this.itemList = itemList;
            this.context = context;
        }

        @Override
        public CommentsAdapter.MyViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments, null);
            CommentsAdapter.MyViewHolders myViewHolders = new CommentsAdapter.MyViewHolders(layoutView);
            return myViewHolders;
        }

        @Override
        public void onBindViewHolder(CommentsAdapter.MyViewHolders holder, final int position) {
//            aQuery.id(holder.profile_img).image(itemList.get(position).getUserImg(), true, true, 0, 0, null, AQuery.FADE_IN);

            if(!TextUtils.isEmpty(itemList.get(position).getUserImg())){
                aQuery.id(holder.profile_img).image(itemList.get(position).getUserImg(), true, true, 0, 0, null, AQuery.FADE_IN);

            }else{
                holder.profile_img.setImageResource(R.drawable.logo_with_whitebg);
            }

            holder.userName.setText(itemList.get(position).getFirstname() + " " + itemList.get(position).getLastname());
            holder.commentText.setText(itemList.get(position).getCommentText());
            String loggedInUserId = Long.toString(SessionUtil.getUserId(CommentsActivity.this));
            if (loggedInUserId.equalsIgnoreCase(itemList.get(position).getUserId())) {
                holder.remove.setVisibility(View.VISIBLE);
            } else {
                holder.remove.setVisibility(View.GONE);
            }
            holder.date.setText(itemList.get(position).getDate());
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeComment(itemList.get(position).getCommentId(), position);
                }
            });

        }

        @Override
        public int getItemCount() {
            return this.itemList.size();
        }

        public class MyViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView profile_img;
            public TextView userName, commentText, date, remove;

            public MyViewHolders(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                profile_img = (ImageView) itemView.findViewById(R.id.profile_img);
                remove = (TextView) itemView.findViewById(R.id.remove_comment);
                userName = (TextView) itemView.findViewById(R.id.userName);
                commentText = (TextView) itemView.findViewById(R.id.comment_text);
                date = (TextView) itemView.findViewById(R.id.comment_date);

            }

            @Override
            public void onClick(View view) {
            }
        }
    }

    private void addNewComment() {
        if (DialogUtil.checkInternetConnection(CommentsActivity.this)) {
            String url = UrlConstants.BASE_URL + UrlConstants.ADD_NEW_COMMENT;
            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            Map params = new HashMap();
            params.put("post_id", getIntent().getStringExtra("post_id"));
            params.put("comment", etAddComment.getText().toString());
            params.put("user_id", SessionUtil.getUserId(CommentsActivity.this));
            aQuery.progress(progress).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    if (json != null ) {
                        try {
                            if (json.getInt("responseCode") == 1) {
                                etAddComment.setText("");
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                getCommentsList();
                            } else {
                                Toast.makeText(CommentsActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(CommentsActivity.this, "Error: "+ status, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            DialogUtil.showNoConnectionDialog(CommentsActivity.this);
        }
    }

    private void removeComment(String commentId, final int position) {
        if (DialogUtil.checkInternetConnection(CommentsActivity.this)) {
            String url = UrlConstants.BASE_URL + UrlConstants.DELETE_COMMENT;
            ProgressDialog progress = new ProgressDialog(CommentsActivity.this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            Map params = new HashMap();
            params.put("comment_id", commentId);
            aQuery.progress(progress).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    if (json != null) {
                        try {
                            if (json.getInt("responseCode") == 1) {
                                Toast.makeText(CommentsActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                commentsList.remove(position);
                                likesAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(CommentsActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(CommentsActivity.this, "Couldn't connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            DialogUtil.showNoConnectionDialog(CommentsActivity.this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                backPress();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        backPress();
        super.onBackPressed();
    }

    private void backPress(){
        Intent i = new Intent();
        i.putExtra("listSize", commentsList.size());
        setResult(RESULT_OK, i);
        finish();
    }
}
