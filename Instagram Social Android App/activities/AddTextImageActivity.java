package com.whaddyalove.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.RelativeLayout;

import com.whaddyalove.R;
import com.whaddyalove.utils.FileUtils;
import com.whaddyalove.view.BubbleInputDialog;
import com.whaddyalove.view.BubbleTextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AddTextImageActivity extends AppCompatActivity {

    private BubbleInputDialog mBubbleInputDialog;
    private BubbleTextView mCurrentEditTextView;
    private ArrayList<View> mViews;
    private RelativeLayout mContentRootView;
    private String encodedImageString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text_image);
        String bitmapString = getIntent().getStringExtra("bitMap");
        byte[] decodedString = Base64.decode(bitmapString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        mContentRootView = (RelativeLayout) findViewById(R.id.rl_content_root);
        Drawable dr = new BitmapDrawable(decodedByte);
        mContentRootView.setBackgroundDrawable(dr);

        findViewById(R.id.imgText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBubble();
            }
        });
        findViewById(R.id.imgDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateBitmap();
            }
        });
        mViews = new ArrayList<>();
        mBubbleInputDialog = new BubbleInputDialog(this);
        mBubbleInputDialog.setCompleteCallBack(new BubbleInputDialog.CompleteCallBack() {
            @Override
            public void onComplete(View bubbleTextView, String str) {
                ((BubbleTextView) bubbleTextView).setText(str);
            }
        });

    }

    //添加气泡
    private void addBubble() {
        final BubbleTextView bubbleTextView = new BubbleTextView(this,
                Color.WHITE, 0);
        bubbleTextView.setImageResource(R.drawable.transparent_opt);
        bubbleTextView.setOperationListener(new BubbleTextView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(bubbleTextView);
                mContentRootView.removeView(bubbleTextView);
            }

            @Override
            public void onEdit(BubbleTextView bubbleTextView) {
                mCurrentEditTextView.setInEdit(false);
                mCurrentEditTextView = bubbleTextView;
                mCurrentEditTextView.setInEdit(true);
            }

            @Override
            public void onClick(BubbleTextView bubbleTextView) {
                mBubbleInputDialog.setBubbleTextView(bubbleTextView);
                mBubbleInputDialog.show();
            }

            @Override
            public void onTop(BubbleTextView bubbleTextView) {
                int position = mViews.indexOf(bubbleTextView);
                if (position == mViews.size() - 1) {
                    return;
                }
                BubbleTextView textView = (BubbleTextView) mViews.remove(position);
                mViews.add(mViews.size(), textView);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mContentRootView.addView(bubbleTextView, lp);
        mViews.add(bubbleTextView);
        setCurrentEdit(bubbleTextView);
    }

    /**
     *
     */
    private void setCurrentEdit(BubbleTextView bubbleTextView) {
        if (mCurrentEditTextView != null) {
            mCurrentEditTextView.setInEdit(false);
        }
        mCurrentEditTextView = bubbleTextView;
        mCurrentEditTextView.setInEdit(true);
    }

    private void generateBitmap() {

        Bitmap bitmap = Bitmap.createBitmap(mContentRootView.getWidth(),
                mContentRootView.getHeight()
                , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mContentRootView.draw(canvas);
        String iamgePath = FileUtils.saveBitmapToLocal(bitmap, this);

        encodeImageToString(bitmap);

    }

    public void encodeImageToString(final Bitmap bitmap) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                encodedImageString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                Intent intent = new Intent();
                intent.putExtra("imagePath", encodedImageString);
                setResult(RESULT_OK, intent);
                finish();
//                captureImageDialog.show();
            }
        }.execute(null, null, null);
    }

}
