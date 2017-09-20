package bwie.com.okhttp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * 文 件 名: MyApplication
 * 创 建 人: 谢兴张
 * 创建日期: 2017/9/20
 * 邮   箱:
 * 博   客:
 * 修改时间：
 * 修改备注：
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    private Context context;
    private List<Bean.TopStoriesBean> mTop_stories;

    public MyAdapter(Context context, List<Bean.TopStoriesBean> top_stories) {
        this.context = context;
        mTop_stories = top_stories;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.texts.setText(mTop_stories.get(position).title);
        holder.texts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnCilckTitle != null) {
                    mOnCilckTitle.setOnCilckItem(mTop_stories, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTop_stories != null ? mTop_stories.size() : 0;
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView texts;

        public MyHolder(View itemView) {
            super(itemView);
            texts = (TextView) itemView.findViewById(R.id.texts);
        }
    }

    private OnCilckTitle mOnCilckTitle;

    public interface OnCilckTitle {
        void setOnCilckItem(List<Bean.TopStoriesBean> mTop_stories, int pos);
    }

    public void setOnCilckTitle(OnCilckTitle onCilckTitle) {
        this.mOnCilckTitle = onCilckTitle;
    }
}