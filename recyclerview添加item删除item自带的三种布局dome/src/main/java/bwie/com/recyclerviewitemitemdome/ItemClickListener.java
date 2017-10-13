package bwie.com.recyclerviewitemitemdome;

import android.view.View;

/**
 * 文 件 名: MyApplication
 * 创 建 人: 谢兴张
 * 创建日期: 2017/10/13
 * 邮   箱:
 * 博   客:
 * 修改时间：
 * 修改备注：
 */

public interface ItemClickListener {
    void onItemSubViewClick(View v, int position);

    //点击回调
    void onItemClick(View v, int position);

    //长按回调
    void onItemLongClick(View v, int position);
}
