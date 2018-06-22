package com.whaddyalove.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private AQuery aQuery;
    private TextView username, noOfLikes, noOfComments, caption, description;
    private ImageView profileImg, postImage, likeBtn, sharePost, addComment, removePost, postLikedIcon;
    private EditText etAddComment;
    private LinearLayout commentLayout;
    private RecyclerView recyclerView;
    private CommentsAdapter commentsAdapter;
    private String post_id, user_id, loggedInUserId, postImagePath;
    private List<CommentsBean> commentsList = new ArrayList<>();
    private List<LikesBean> likesList = new ArrayList<>();
    private String shareImageUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        user_id = getIntent().getStringExtra("user_id");
        post_id = getIntent().getStringExtra("post_id");
        aQuery = new AQuery(this);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        TextView title = (TextView) toolBar.findViewById(R.id.title);
        title.setText("Photo");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        upArrow.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        inflateXml();
        //getPostData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPostData();
    }

    private void inflateXml() {
        username = (TextView) findViewById(R.id.userName);
        noOfLikes = (TextView) findViewById(R.id.noOfLikes);
        noOfComments = (TextView) findViewById(R.id.noOfComments);
        caption = (TextView) findViewById(R.id.caption);
        description = (TextView) findViewById(R.id.description);
        removePost = (ImageView) findViewById(R.id.removePost);
        profileImg = (ImageView) findViewById(R.id.profile_img);
        postImage = (ImageView) findViewById(R.id.postImg);
        postLikedIcon = (ImageView) findViewById(R.id.liked_img);
        likeBtn = (ImageView) findViewById(R.id.likeBtn);
        sharePost = (ImageView) findViewById(R.id.sharePost);
        addComment = (ImageView) findViewById(R.id.add_comment_btn);
        etAddComment = (EditText) findViewById(R.id.et_add_comment);
        commentLayout = (LinearLayout) findViewById(R.id.commentsLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        loggedInUserId = Long.toString(SessionUtil.getUserId(PostActivity.this));
        if (loggedInUserId.equalsIgnoreCase(user_id)) {
            removePost.setVisibility(View.VISIBLE);
        } else {
            removePost.setVisibility(View.GONE);
        }

        sharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) postImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                // Save this bitmap to a file.
                File cache = getApplicationContext().getExternalCacheDir();
                File sharefile = new File(cache, "whaddiyalove.png");
                try {
                    FileOutputStream out = new FileOutputStream(sharefile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (IOException e) {

                }

                // Now send it out to share
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + sharefile));
                try {
                    startActivity(Intent.createChooser(share, "Share photo Whaddya Love"));
                } catch (Exception e) {

                }


            }
        });

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likePost();
            }
        });

        noOfLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (likesList.size() > 0) {
                    Intent i = new Intent(PostActivity.this, LikesActivity.class);
                    i.putExtra("post_id", post_id);
                    i.putExtra("likes_list", (Serializable) likesList);
                    startActivity(i);
                }
            }
        });

        noOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentsList.size() > 0) {
                    Intent i = new Intent(PostActivity.this, CommentsActivity.class);
                    i.putExtra("post_id", post_id);
                    i.putExtra("comments_list", (Serializable) commentsList);
                    startActivity(i);
                }
            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAddComment.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(PostActivity.this, "Please enter your comment first.", Toast.LENGTH_SHORT).show();
                } else {
                    addNewComment();
                }

            }
        });

        removePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePost();
            }
        });
    }

    private void likePost() {
        if (DialogUtil.checkInternetConnection(PostActivity.this)) {
            String url = UrlConstants.BASE_URL + UrlConstants.LIKE_POST;
            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            Map params = new HashMap();
            params.put("user_id", SessionUtil.getUserId(PostActivity.this));
            params.put("post_id", post_id);
            aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    if (json != null ) {
                        try {
                            if (json.getInt("responseCode") == 1) {
                                Animation pulse_fade = AnimationUtils.loadAnimation(PostActivity.this, R.anim.pulse_fade_in);
                                pulse_fade.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                        postLikedIcon.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        postLikedIcon.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                postLikedIcon.startAnimation(pulse_fade);
                                likeBtn.setEnabled(false);
                                likeBtn.setImageResource(R.drawable.logo_liked);
                                if (likesList.size() > 0) {
                                    int like = (likesList.size() + 1);
                                    noOfLikes.setText(like + " loves");
                                } else {
                                    int like = (0 + 1);
                                    noOfLikes.setText(like + " loves");
                                }

                            } else {
                                Toast.makeText(PostActivity.this, "Error: " + json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(PostActivity.this, "Couldn't connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            DialogUtil.showNoConnectionDialog(PostActivity.this);
        }
    }

    public void animateHeart(final ImageView view) {
        postLikedIcon.setVisibility(View.VISIBLE);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        prepareAnimation(scaleAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        prepareAnimation(alphaAnimation);

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(alphaAnimation);
        animation.addAnimation(scaleAnimation);
        animation.setDuration(1200);
        animation.setFillAfter(true);

        view.startAnimation(animation);
        getPostData();

    }

    private Animation prepareAnimation(Animation animation){
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }

    private void deletePost() {
        if (DialogUtil.checkInternetConnection(PostActivity.this)) {
            String url = UrlConstants.BASE_URL + UrlConstants.DELETE_POST;
            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            Map params = new HashMap();
            params.put("post_id", post_id);
            aQuery.progress(progress).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    if (json != null ) {
                        try {
                            if (json.getInt("responseCode") == 1) {
                                Toast.makeText(PostActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                PostActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(PostActivity.this, "Couldn't connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            DialogUtil.showNoConnectionDialog(PostActivity.this);
        }
    }

    private void getPostData() {
        if (DialogUtil.checkInternetConnection(PostActivity.this)) {
            String url = UrlConstants.BASE_URL + UrlConstants.GET_POST_DETAILS;
            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            Map params = new HashMap();
            params.put("post_id", post_id);
            aQuery.progress(progress).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    if (json != null ) {
                        try {
                            if (json.getInt("responseCode") == 1) {
                                JSONObject infoObj = json.getJSONObject("data");
                                shareImageUrl = infoObj.getString("post_image");
                                commentsList = ConversionHelper.getCommentsList(infoObj);
                                likesList = ConversionHelper.getLikesList(infoObj);
                                username.setText(infoObj.getString("firstname") + " " + infoObj.getString("lastname"));
                                postImagePath = infoObj.getString("post_image");
                                aQuery.id(profileImg).image(infoObj.getString("profile_pic"), true, true, 0, 0, null, AQuery.FADE_IN);
                                aQuery.id(postImage).image(infoObj.getString("post_image"), true, true, 0, 0, null, AQuery.FADE_IN);
                                caption.setText(infoObj.getString("caption"));
                                description.setText(infoObj.getString("description"));
                                noOfComments.setText(commentsList.size() + " comments");
                                for(LikesBean data : likesList){
                                    if (data.getUserId().equalsIgnoreCase(SessionUtil.getUserId(PostActivity.this)+"")){
                                        likeBtn.setEnabled(false);
                                        likeBtn.setImageResource(R.drawable.logo_liked);
                                    }
                                }
                                noOfLikes.setText(likesList.size() + " loves");
                                commentsAdapter = new CommentsAdapter(PostActivity.this, commentsList);
                                recyclerView.setAdapter(commentsAdapter);
                            } else {
                                Toast.makeText(PostActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(PostActivity.this, "Couldn't connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            DialogUtil.showNoConnectionDialog(PostActivity.this);
        }
    }

    private void addNewComment() {
        if (DialogUtil.checkInternetConnection(PostActivity.this)) {
            String url = UrlConstants.BASE_URL + UrlConstants.ADD_NEW_COMMENT;
            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            Map params = new HashMap();
            params.put("post_id", post_id);
            params.put("comment", etAddComment.getText().toString());
            params.put("user_id", SessionUtil.getUserId(PostActivity.this));
//            params.put("user_id", 9);
            aQuery.progress(progress).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    if (json != null ) {
                        try {
                            if (json.getInt("responseCode") == 1) {
                                getPostData();
                                etAddComment.setText("");
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            } else {
                                Toast.makeText(PostActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(PostActivity.this, "Error: "+ status, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            DialogUtil.showNoConnectionDialog(PostActivity.this);
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

            if (loggedInUserId.equalsIgnoreCase(itemList.get(position).getUserId())) {
                holder.remove.setVisibility(View.VISIBLE);
            } else {
                holder.remove.setVisibility(View.GONE);
            }
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeComment(itemList.get(position).getCommentId());
                }
            });
            holder.date.setText(itemList.get(position).getDate());
        }

        private void removeComment(String commentId) {
            if (DialogUtil.checkInternetConnection(PostActivity.this)) {
                String url = UrlConstants.BASE_URL + UrlConstants.DELETE_COMMENT;
                ProgressDialog progress = new ProgressDialog(PostActivity.this);
                progress.setMessage("Loading...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                Map params = new HashMap();
                params.put("comment_id", commentId);
                aQuery.progress(progress).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        if (json != null ) {
                            try {
                                if (json.getInt("responseCode") == 1) {
                                    Toast.makeText(PostActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                    commentsAdapter.notifyDataSetChanged();
                                    getPostData();
                                } else {
                                    Toast.makeText(PostActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(PostActivity.this, "Couldn't connect to server. Please try again later.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                DialogUtil.showNoConnectionDialog(PostActivity.this);
            }
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
